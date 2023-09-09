package com.islog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.islog.api.domain.Post;
import com.islog.api.repository.PostRepository;
import com.islog.api.request.PostCreate;
import com.islog.api.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
// Mock 객체 자동 주입
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
//    @WebMvcTest를 넣으면 MockMvc가 주입이 된다.

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
//        글 제목
//        글 내용
//        PostCreate request = new PostCreate("제목입니다.", "내용입니다.");
        // 대신
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        // builder 패턴을 사용한 방법

        String json = objectMapper.writeValueAsString(request);
        // @Getter가 있으면 java Bean 규약에 따라 json 형태로 만들어준다!

//        System.out.println(json);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                                .contentType(APPLICATION_JSON)
//                        .param("title", "글 제목입니다.")
//                        .param("content", "글 내용입니다."))
                                .content(json)
                        // .content의 매개변수는 String만 들어가기 때문에 request 객체를 content
                        // 즉, body에 담아서 보내기 위해선, objectMapper를 통해 변환한 후, 매개변수에 담아서 전달하면 된다.

//                이전에 만들었던 PostCreate 클래스에 값이 들어가지 않는다...
//                -> body로 값이 넘어가기 때문에 해당 url에 @RequestBody로 값을 받아야 한다. (@RequestBody PostCreate params)
                )
                .andExpect(status().isOk())
                .andDo(print());
//        HTTP에 대한 summary를 남겨주게 된다.
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {

        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

//        expectd
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                                .contentType(APPLICATION_JSON)
//                {"title": ""} 검증이 정상적으로 되었다.
//                                .content("{\"title\": \"\", \"content\": \"내용입니다.\"}")
                                .content(json)
//                {"title": null}일 때
                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
//                json 검증 방법에 좋은 것
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("title을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다..")
    void test3() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                                .contentType(APPLICATION_JSON)
//                                .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                                .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("insoo")
                .build();
        postRepository.save(post);

        //클라이언트 요구사항
        //json 응답에서 title값 길이를 최대 10글자로 해주세요.

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("insoo"))
                .andDo(print());
    }

//    @Test
//    @DisplayName("글 여러개 조회")
//    void test5() throws Exception {
//
//        //given
//        Post post1 = Post.builder()
//                .title("Title_1")
//                .content("Content_1")
//                .build();
//        postRepository.save(post1);
//
//        Post post2 = Post.builder()
//                .title("Title_2")
//                .content("Content_2")
//                .build();
//        postRepository.save(post2);
//
//        //클라이언트 요구사항
//        //json 응답에서 title값 길이를 최대 10글자로 해주세요.
//
//        //expected
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", Matchers.is(2)))
//                .andExpect(jsonPath("$[0].id").value(post1.getId()))
//                .andExpect(jsonPath("$[0].title").value("Title_1"))
//                .andExpect(jsonPath("$[0].content").value("Content_1"))
//                .andExpect(jsonPath("$[1].id").value(post2.getId()))
//                .andExpect(jsonPath("$[1].title").value("Title_2"))
//                .andExpect(jsonPath("$[1].content").value("Content_2"))
//                .andDo(print());
//    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("Title : " + i)
                        .content("Content : " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id").value(requestPosts.get(requestPosts.size() - 1).getId()))
                .andExpect(jsonPath("$[0].title").value("Title : 30"))
                .andExpect(jsonPath("$[0].content").value("Content : 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("Title : " + i)
                        .content("Content : " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=0&size=5")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(5)))
                .andExpect(jsonPath("$[0].id").value(requestPosts.get(requestPosts.size() - 1).getId()))
                .andExpect(jsonPath("$[0].title").value("Title : 30"))
                .andExpect(jsonPath("$[0].content").value("Content : 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 변경")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                .title("BIS")
                .content("상도더샵")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("BIS")
                .content("반포자이")
                .build();

        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId()) // PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))

                .andExpect(status().isOk()) // MockMvcResultMatchers
                .andDo(print()); // MockMvcResultHandlers
    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {

        //given
        Post post = Post.builder()
                .title("BIS")
                .content("상도더샵")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {

        // expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {

        //given
        PostEdit postEdit = PostEdit.builder()
                .title("BIS")
                .content("반포자이")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    void test11() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}

// API 문서 생성

// GET /posts/{postId} -> 단건 조회
// POST /posts -> 게시글 등록

// 클라이언트 입장 어떤 API 있는지 모름

// Spring RestDocs
// 운영코드에 영향이 없다.
// 코드 수정 -> 문서를 수정 X -> 코드(기능) <-> 문서
// Test 케이스 실행 -> 통과가 되면 문서를 자동으로 생성


