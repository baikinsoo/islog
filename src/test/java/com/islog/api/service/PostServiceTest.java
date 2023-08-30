package com.islog.api.service;

import com.islog.api.domain.Post;
import com.islog.api.repository.PostRepository;
import com.islog.api.request.PostCreate;
import com.islog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        //when
        PostResponse postResponse = postService.get(requestPost.getId());
        //
        assertNotNull(requestPost);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", requestPost.getTitle());
        assertEquals("bar", requestPost.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {

        //given
//        Post requestPost1 = Post.builder()
//                .title("title 1")
//                .content("content 1")
//                .build();
//        postRepository.save(requestPost1);
//
//        Post requestPost2 = Post.builder()
//                .title("title 2")
//                .content("content 2")
//                .build();
//        postRepository.save(requestPost2);

        //위에서 2번을 저장하던걸 한번에 저장할 수 있다.

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                            .title("Title : " + i)
                            .content("Content : " + i)
                            .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");

        //when
        List<PostResponse> postResponses = postService.getList(pageable);

        //then
        assertEquals(5L, postResponses.size());
        assertEquals("Title : 30", postResponses.get(0).getTitle());
        assertEquals("Title : 26", postResponses.get(4).getTitle());
    }
}