package com.islog.api.service;

import com.islog.api.domain.Post;
import com.islog.api.repository.PostRepository;
import com.islog.api.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post write(PostCreate postCreate) {
//        postCreate -> Entity
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        return postRepository.save(post);
    }
    
    public Post get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
//        최초에 Optional을 감싸서 반환한다 -> 값이 없을 경우 null을 반환하기 때문이다.
//        글이 없으면 없다는 적당한 에러를 만드는 것이 좋다.
        return post;
    }
}
