package com.islog.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
//    @WebMvcTest를 넣으면 MockMvc가 주입이 된다.

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
//        글 제목
//        글 내용

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("title", "글 제목입니다.")
//                        .param("content", "글 내용입니다."))
                                .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
//                이전에 만들었던 PostCreate 클래스에 값이 들어가지 않는다...
//                -> body로 값이 넘어가기 때문에 해당 url에 @RequestBody로 값을 받아야 한다. (@RequestBody PostCreate params)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{}"))
                .andDo(MockMvcResultHandlers.print());
//        HTTP에 대한 summary를 남겨주게 된다.
//
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {
//        expectd
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
//                {"title": ""} 검증이 정상적으로 되었다.
//                                .content("{\"title\": \"\", \"content\": \"내용입니다.\"}")
                                .content("{\"title\": null, \"content\": \"내용입니다.\"}")
//                {"title": null}일 때
                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
//                json 검증 방법에 좋은 것
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("title을 입력해주세요"))
                .andDo(MockMvcResultHandlers.print());
    }
}