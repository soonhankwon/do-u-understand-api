package com.douunderstandapi.subscribe.dto.response;

import com.douunderstandapi.post.dto.PostDTO;
import java.util.List;

public record SubscribePostsGetResponse(
        int totalPageCount,
        List<PostDTO> postList
) {
}
