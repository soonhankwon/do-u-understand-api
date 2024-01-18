package com.douunderstandapi.post.dto.response;

import com.douunderstandapi.post.dto.PostDTO;
import java.util.List;

public record PostsGetResponse(
        int totalPageCount,
        List<PostDTO> postList
) {
}
