package kr.artner.domain.artist.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.artner.domain.artist.enums.RoleCode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class RoleCodeArrayConverter implements AttributeConverter<List<RoleCode>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<RoleCode> roleCodes) {
        if (roleCodes == null || roleCodes.isEmpty()) {
            return null;
        }
        return roleCodes.stream()
                .map(Enum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<RoleCode> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(String::trim)
                .map(RoleCode::valueOf)
                .collect(Collectors.toList());
    }
}