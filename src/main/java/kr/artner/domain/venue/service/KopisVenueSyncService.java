package kr.artner.domain.venue.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KopisVenueSyncService {

    private final VenueRepository venueRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public KopisVenueSyncService(VenueRepository venueRepository, ObjectMapper objectMapper) {
        this.venueRepository = venueRepository;
        this.objectMapper = objectMapper;

        // 시스템 프로퍼티 설정으로 네트워크 문제 해결 시도
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("java.net.preferIPv4Stack", "true");

        this.restTemplate = new RestTemplate();
    }

    @Value("${kopis.api.key:}")
    private String kopisApiKey;

    @Value("${kopis.api.base-url:http://kopis.or.kr/openApi/restful}")
    private String kopisBaseUrl;

    private static final String VENUE_LIST_ENDPOINT = "/prfplc";
    private static final String VENUE_DETAIL_ENDPOINT = "/prfplc/";

    @Scheduled(cron = "0 0 2 * * ?") // 매일 새벽 2시에 실행
    @Transactional
    public void syncVenueData() {
        if (kopisApiKey == null || kopisApiKey.isEmpty()) {
            log.warn("KOPIS API key not configured. Skipping venue sync.");
            return;
        }

        log.info("Starting KOPIS venue data synchronization...");

        try {
            // KOPIS에서 전체 공연시설 데이터 가져오기
            List<KopisVenueDetail> kopisVenues = fetchAllVenueDetails();

            if (kopisVenues.isEmpty()) {
                log.warn("No venue data retrieved from KOPIS API");
                return;
            }

            // 기존 KOPIS 공연장 데이터를 Map으로 변환 (kopisVenueId -> Venue)
            List<Venue> existingVenues = venueRepository.findBySource("KOPIS");
            Map<String, Venue> existingVenueMap = existingVenues.stream()
                    .filter(v -> v.getKopisVenueId() != null)
                    .collect(Collectors.toMap(Venue::getKopisVenueId, v -> v));

            int createdCount = 0;
            int updatedCount = 0;
            int unchangedCount = 0;

            for (KopisVenueDetail kopisVenue : kopisVenues) {
                Venue existingVenue = existingVenueMap.get(kopisVenue.getId());

                if (existingVenue == null) {
                    // 새로운 공연장 생성
                    Venue newVenue = convertToVenue(kopisVenue);
                    if (newVenue != null) {
                        venueRepository.save(newVenue);
                        createdCount++;
                    } else {
                        log.warn("Failed to create venue from KOPIS data: {}", kopisVenue.getId());
                    }
                } else {
                    // 기존 공연장과 비교하여 변경사항 있으면 업데이트
                    if (hasVenueChanged(existingVenue, kopisVenue)) {
                        updateVenueFromKopis(existingVenue, kopisVenue);
                        venueRepository.save(existingVenue);
                        updatedCount++;
                    } else {
                        unchangedCount++;
                    }
                    // 처리된 공연장은 Map에서 제거
                    existingVenueMap.remove(kopisVenue.getId());
                }
            }

            // Map에 남은 공연장들은 KOPIS에서 삭제된 것으로 간주
            int deprecatedCount = 0;
            for (Venue remainingVenue : existingVenueMap.values()) {
                // 소프트 삭제: 이름에 DEPRECATED 표시
                remainingVenue.updateVenue(
                    remainingVenue.getName() + " (DEPRECATED)",
                    remainingVenue.getRegion(),
                    remainingVenue.getAddress(),
                    remainingVenue.getImageUrl(),
                    remainingVenue.getSeatCapacity(),
                    remainingVenue.getBaseRentalFee(),
                    remainingVenue.getDescription()
                );
                venueRepository.save(remainingVenue);
                deprecatedCount++;
            }

            log.info("KOPIS venue sync completed. Created: {}, Updated: {}, Unchanged: {}, Deprecated: {}",
                    createdCount, updatedCount, unchangedCount, deprecatedCount);

        } catch (Exception e) {
            log.error("Failed to sync venue data from KOPIS", e);
        }
    }

    private boolean hasVenueChanged(Venue existingVenue, KopisVenueDetail kopisVenue) {
        return !Objects.equals(existingVenue.getName(), kopisVenue.getFacilityName()) ||
               !Objects.equals(existingVenue.getRegion(), kopisVenue.getArea()) ||
               !Objects.equals(existingVenue.getAddress(), kopisVenue.getAddress()) ||
               !Objects.equals(existingVenue.getSeatCapacity(), kopisVenue.getSeatingCapacity()) ||
               !Objects.equals(existingVenue.getFacilityType(), kopisVenue.getFacilityType()) ||
               !Objects.equals(existingVenue.getDescription(), kopisVenue.getDescription());
    }

    private void updateVenueFromKopis(Venue venue, KopisVenueDetail kopisVenue) {
        venue.updateVenue(
            kopisVenue.getFacilityName(),
            kopisVenue.getArea(),
            kopisVenue.getAddress(),
            venue.getImageUrl(), // 기존 이미지 URL 유지
            kopisVenue.getSeatingCapacity(),
            venue.getBaseRentalFee(), // 기존 대관비 유지
            kopisVenue.getDescription()
        );
    }

    private Venue convertToVenue(KopisVenueDetail kopisVenue) {
        // 필수 필드 validation
        String name = kopisVenue.getFacilityName();
        if (name == null || name.trim().isEmpty()) {
            log.warn("Venue name is null or empty for venue ID: {}", kopisVenue.getId());
            return null;
        }

        String region = kopisVenue.getArea();
        if (region == null || region.trim().isEmpty()) {
            region = "정보없음"; // 기본값 설정
        }

        return Venue.builder()
                .name(name.trim())
                .region(region.trim())
                .address(kopisVenue.getAddress())
                .seatCapacity(kopisVenue.getSeatingCapacity())
                .facilityType(kopisVenue.getFacilityType())
                .description(kopisVenue.getDescription())
                .kopisVenueId(kopisVenue.getId())
                .source("KOPIS")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private List<KopisVenueDetail> fetchAllVenueDetails() {
        List<KopisVenueDetail> allVenues = new ArrayList<>();
        int currentPage = 1;
        int rowsPerPage = 100;

        while (true) {
            List<KopisVenueListItem> venueList = fetchVenueList(currentPage, rowsPerPage);

            if (venueList.isEmpty()) {
                break;
            }

            // venue list에서 바로 상세 정보 생성 (API 호출 최소화)
            for (KopisVenueListItem venueItem : venueList) {
                try {
                    KopisVenueDetail venueDetail = convertListItemToDetail(venueItem);
                    if (venueDetail != null) {
                        allVenues.add(venueDetail);
                    }
                } catch (Exception e) {
                    log.error("Failed to convert venue item: {}", venueItem.getId(), e);
                }
            }

            // 페이지당 rows 수보다 적게 왔으면 마지막 페이지
            if (venueList.size() < rowsPerPage) {
                break;
            }

            currentPage++;
        }

        return allVenues;
    }

    private List<KopisVenueListItem> fetchVenueList(int page, int rows) {
        // KOPIS API는 XML을 반환하므로 XML로 파싱해야 함
        String url = kopisBaseUrl + VENUE_LIST_ENDPOINT + "?service=" + kopisApiKey + "&rows=" + rows + "&cpage=" + page;

        log.info("Requesting KOPIS venue list with URL: {}", url.replace(kopisApiKey, "***"));

        try {
            // curl을 사용해서 API 호출 (XML 응답 요청)
            ProcessBuilder processBuilder = new ProcessBuilder(
                "curl", "-s", url
            );
            Process process = processBuilder.start();

            StringBuilder response = new StringBuilder();
            StringBuilder errorResponse = new StringBuilder();

            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
                 var errorReader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String xmlResponse = response.toString();
                log.info("KOPIS API XML response: {}", xmlResponse);

                // XML 에러 체크
                if (xmlResponse.contains("<errmsg>") || xmlResponse.contains("ERROR")) {
                    log.error("KOPIS API returned error: {}", xmlResponse);
                    return List.of();
                }

                // XML에서 데이터 추출 (간단한 방식 - 실제 구현에서는 XML 파서 사용 권장)
                return parseVenueListFromXml(xmlResponse);

            } else {
                log.error("Curl command failed with exit code: {}. Error: {}", exitCode, errorResponse.toString());
            }

        } catch (Exception e) {
            log.error("Failed to fetch venue list from KOPIS using curl", e);
        }

        return List.of();
    }

    private KopisVenueDetail fetchVenueDetail(String venueId) {
        String url = kopisBaseUrl + VENUE_DETAIL_ENDPOINT + venueId + "?service=" + kopisApiKey;

        log.debug("Requesting KOPIS venue detail with URL: {}", url.replace(kopisApiKey, "***"));

        try {
            // curl을 사용해서 API 호출 (XML 응답)
            ProcessBuilder processBuilder = new ProcessBuilder(
                "curl", "-s", url
            );
            Process process = processBuilder.start();

            StringBuilder response = new StringBuilder();
            StringBuilder errorResponse = new StringBuilder();

            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
                 var errorReader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String xmlResponse = response.toString();
                log.debug("KOPIS venue detail XML response: {}", xmlResponse);

                // XML 에러 체크
                if (xmlResponse.contains("<errmsg>") || xmlResponse.contains("ERROR")) {
                    log.error("KOPIS API returned error for venue {}: {}", venueId, xmlResponse);
                    return null;
                }

                return parseVenueDetailFromXml(xmlResponse);

            } else {
                log.error("Curl command failed with exit code: {} for venue ID: {}. Error: {}", exitCode, venueId, errorResponse.toString());
            }

        } catch (Exception e) {
            log.error("Failed to fetch venue detail for ID: {} using curl", venueId, e);
        }

        return null;
    }

    private KopisVenueDetail convertListItemToDetail(KopisVenueListItem item) {
        KopisVenueDetail detail = new KopisVenueDetail();
        detail.setId(item.getId());
        detail.setFacilityName(item.getFacilityName());
        detail.setArea(item.getArea());
        // 나머지 필드들은 기본값으로 설정하거나 추후 상세 API로 보완 가능
        return detail;
    }

    private List<KopisVenueListItem> parseVenueListFromXml(String xmlResponse) {
        List<KopisVenueListItem> venues = new ArrayList<>();

        // <db> 태그들을 찾아서 각각을 파싱
        Pattern dbPattern = Pattern.compile("<db>(.*?)</db>", Pattern.DOTALL);
        Matcher dbMatcher = dbPattern.matcher(xmlResponse);

        while (dbMatcher.find()) {
            String dbContent = dbMatcher.group(1);

            // 에러 메시지가 있는지 확인
            if (dbContent.contains("<errmsg>") || dbContent.contains("<returncode>")) {
                continue; // 에러 항목은 건너뛰기
            }

            KopisVenueListItem venue = new KopisVenueListItem();

            // KOPIS venue list API에서는 mt10id가 venue ID 역할을 함
            String id = extractXmlValue(dbContent, "mt10id");
            if (id != null) venue.setId(id);

            // fcltynm 추출
            String facilityName = extractXmlValue(dbContent, "fcltynm");
            if (facilityName != null) venue.setFacilityName(facilityName);

            // sidonm 추출 (시도명 - 지역)
            String area = extractXmlValue(dbContent, "sidonm");
            if (area != null) venue.setArea(area);

            // 유효한 데이터가 있는 경우에만 추가
            if (venue.getId() != null && venue.getFacilityName() != null) {
                venues.add(venue);
                log.debug("Parsed venue: ID={}, Name={}, Area={}", venue.getId(), venue.getFacilityName(), venue.getArea());
            }
        }

        log.info("Parsed {} venues from XML response", venues.size());
        return venues;
    }

    private KopisVenueDetail parseVenueDetailFromXml(String xmlResponse) {
        // <db> 태그 내용 추출
        Pattern dbPattern = Pattern.compile("<db>(.*?)</db>", Pattern.DOTALL);
        Matcher dbMatcher = dbPattern.matcher(xmlResponse);

        if (!dbMatcher.find()) {
            return null;
        }

        String dbContent = dbMatcher.group(1);

        // 에러 메시지가 있는지 확인
        if (dbContent.contains("<errmsg>") || dbContent.contains("<returncode>")) {
            return null;
        }

        KopisVenueDetail venue = new KopisVenueDetail();

        // venue detail에서는 venue ID를 그대로 사용 (list에서 가져온 mt10id)
        venue.setId(extractXmlValue(dbContent, "mt20id"));
        if (venue.getId() == null) {
            // mt20id가 없으면 mt10id 사용
            venue.setId(extractXmlValue(dbContent, "mt10id"));
        }
        venue.setFacilityName(extractXmlValue(dbContent, "fcltynm"));
        venue.setArea(extractXmlValue(dbContent, "sidonm")); // 시도명 사용
        venue.setAddress(extractXmlValue(dbContent, "adres"));
        venue.setLatitude(extractXmlValue(dbContent, "la"));
        venue.setLongitude(extractXmlValue(dbContent, "lo"));
        venue.setSeatingScale(extractXmlValue(dbContent, "seatscale"));
        venue.setPhoneNumber(extractXmlValue(dbContent, "telno"));
        venue.setWebsite(extractXmlValue(dbContent, "relateurl"));
        venue.setFacilityTypeCode(extractXmlValue(dbContent, "fcltytypecode"));
        venue.setFacilityType(extractXmlValue(dbContent, "fcltytypename"));

        return venue;
    }

    private String extractXmlValue(String xml, String tagName) {
        Pattern pattern = Pattern.compile("<" + tagName + ">(.*?)</" + tagName + ">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xml);
        if (matcher.find()) {
            String value = matcher.group(1).trim();
            return value.isEmpty() ? null : value;
        }
        return null;
    }

    // KOPIS API Response DTOs
    public static class KopisVenueListResponse {
        @JsonProperty("db")
        private List<KopisVenueListItem> db;

        public List<KopisVenueListItem> getDb() { return db; }
        public void setDb(List<KopisVenueListItem> db) { this.db = db; }
    }

    public static class KopisVenueListItem {
        @JsonProperty("mt20id")
        private String id;

        @JsonProperty("fcltynm")
        private String facilityName;

        @JsonProperty("mt10id")
        private String area;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getFacilityName() { return facilityName; }
        public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
        public String getArea() { return area; }
        public void setArea(String area) { this.area = area; }
    }

    public static class KopisVenueDetailResponse {
        @JsonProperty("db")
        private KopisVenueDetail db;

        public KopisVenueDetail getDb() { return db; }
        public void setDb(KopisVenueDetail db) { this.db = db; }
    }

    public static class KopisVenueDetail {
        @JsonProperty("mt20id")
        private String id;

        @JsonProperty("fcltynm")
        private String facilityName;

        @JsonProperty("mt10id")
        private String area;

        @JsonProperty("adres")
        private String address;

        @JsonProperty("la")
        private String latitude;

        @JsonProperty("lo")
        private String longitude;

        @JsonProperty("seatscale")
        private String seatingScale;

        @JsonProperty("telno")
        private String phoneNumber;

        @JsonProperty("relateurl")
        private String website;

        @JsonProperty("fcltytypecode")
        private String facilityTypeCode;

        @JsonProperty("fcltytypename")
        private String facilityType;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getFacilityName() { return facilityName; }
        public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
        public String getArea() { return area; }
        public void setArea(String area) { this.area = area; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getLatitude() { return latitude; }
        public void setLatitude(String latitude) { this.latitude = latitude; }
        public String getLongitude() { return longitude; }
        public void setLongitude(String longitude) { this.longitude = longitude; }
        public String getSeatingScale() { return seatingScale; }
        public void setSeatingScale(String seatingScale) { this.seatingScale = seatingScale; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getWebsite() { return website; }
        public void setWebsite(String website) { this.website = website; }
        public String getFacilityTypeCode() { return facilityTypeCode; }
        public void setFacilityTypeCode(String facilityTypeCode) { this.facilityTypeCode = facilityTypeCode; }
        public String getFacilityType() { return facilityType; }
        public void setFacilityType(String facilityType) { this.facilityType = facilityType; }

        public Integer getSeatingCapacity() {
            if (seatingScale == null || seatingScale.trim().isEmpty()) {
                return null;
            }
            try {
                // "1,000석" 형태에서 숫자만 추출
                String numbers = seatingScale.replaceAll("[^0-9]", "");
                return numbers.isEmpty() ? null : Integer.parseInt(numbers);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        public String getDescription() {
            StringBuilder desc = new StringBuilder();
            if (facilityType != null) desc.append("시설유형: ").append(facilityType);
            if (phoneNumber != null) {
                if (desc.length() > 0) desc.append("\n");
                desc.append("연락처: ").append(phoneNumber);
            }
            if (website != null) {
                if (desc.length() > 0) desc.append("\n");
                desc.append("웹사이트: ").append(website);
            }
            return desc.length() > 0 ? desc.toString() : null;
        }
    }
}