package com.islog.api.repository;

import com.islog.api.domain.Post;
import com.islog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
