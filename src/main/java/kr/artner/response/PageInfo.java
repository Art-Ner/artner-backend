package kr.artner.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private long totalCount;
    private int limit;
    private long offset;
    private boolean hasMore;
}
