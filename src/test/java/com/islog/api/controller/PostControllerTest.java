package com.islog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.islog.api.domain.Post;
import com.islog.api.repository.PostRepository;
import com.islog.api.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
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
                                .content(json) // String만 들어간다.
//                이전에 만들었던 PostCreate 클래스에 값이 들어가지 않는다...
//                -> body로 값이 넘어가기 때문에 해당 url에 @RequestBody로 값을 받아야 한다. (@RequestBody PostCreate params)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
//        HTTP에 대한 summary를 남겨주게 된다.
//
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
                .title("name")
                .content("insoo")
                .build();

        postRepository.save(post);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("name"))
                .andExpect(jsonPath("$.content").value("insoo"))
                .andDo(print());

    }
}