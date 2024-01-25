package com.douunderstandapi.subscribe.dto.response;

import com.douunderstandapi.post.dto.PostDTO;
import java.util.List;

public record SubscribePostsGetResponse(
        int totalPageCount,
        List<PostDTO> postList
) {
    public static SubscribePostsGetResponse of(int totalPages, List<PostDTO> postDTOS) {
        return new SubscribePostsGetResponse(totalPages, postDTOS);
    }
}
