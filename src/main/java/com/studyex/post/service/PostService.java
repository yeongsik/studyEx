package com.studyex.post.service;

import com.studyex.post.dto.ViewPostRequest;
import com.studyex.post.dto.WritePostRequest;
import com.studyex.post.dto.ViewPostResponse;

public interface PostService {
    void createPost(WritePostRequest writePostRequest);

    ViewPostResponse getPostView(ViewPostRequest viewPostRequest);

}
