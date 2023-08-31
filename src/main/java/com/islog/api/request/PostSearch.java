package com.islog.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
//@Builder.Default를 사용하려면 class 단에서 @Builder를 사용해야 한다.
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

//    @Builder
////    public PostSearch(int page, int size) {
//    public PostSearch(Integer page, Integer size) {
//        this.page = page;
//        this.size = size;
//    }

    public long getOffset() {
        return (long) (Math.max(1,page) - 1) * Math.min(size, MAX_SIZE);
    }
}
