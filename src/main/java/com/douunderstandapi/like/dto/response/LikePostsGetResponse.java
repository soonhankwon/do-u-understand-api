package com.douunderstandapi.like.dto.response;

import com.douunderstandapi.post.dto.PostDTO;
import java.util.List;

public record LikePostsGetResponse(
        int totalPageCount,
        List<PostDTO> postList
) {
}
