package com.islog.api.controller;

import com.islog.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class PostController {

//    Http Method
//    Get, Post, put, patch, delete, options, head, trace, connect -> 이게 다 뭐하는지 알아야 한다.
// 글 등록
//    post Method
    @PostMapping("/posts")
//    public String post() {
//    public String post(@RequestParam String title, @RequestParam String content) {
////        log.info("title={}, content={}", title, content);
//        public String post(@RequestParam Map<String, String> params) {
//        log.info("params={}", params);
//        String title = params.get("title");
//        -> Map을 위의 두 형태처럼 만드는 것도 있고, Map 형태를 클래스로 따로 만드는 방법이 있다.
    public Map<String, String> post(@RequestBody @Valid PostCreate params) throws Exception {
//        데이터를 검증하는 이유
//        1. client 개발자가 깜빡할 수 있다. 실수로 값을 안보낼 수 있다.
//        2. client bug로 값이 누락될 수 있다.
//        3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
//        4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
//        5. 서버 개발자의 편안함을 위해서...ㅎ
        log.info("params={}", params.toString());

//        String title = params.getTitle();
//        if (title == null || title.equals("")) {
////            error
////            1. 빡쎄다. (노가다)
////            2. 개발팁 -> 무언가 3번 이상 반복 작업을 할 때 내가 뭔가 잘못하고 있는건 아닐지 의심한다.
////            3. 누락 가능성
////            4. 생각보다 검증해야 할 것이 많다. (꼼꼼하지 않을 수 있다.)
//
//            throw new Exception("타이틀값이 없어요!");
//        }
//        String content = params.getContent();
//        if (content == null || content.equals("")) {
////            error
//        }

//        ------------ json 형식으로 값이 없다는 것을 알려주고 싶을 때
//        파라미터 옆에 bindingresult를 달아준다.

//        ------------
//        1. 매번 메서드마다 값을 검증 해야한다.
//          > 개발자가 까먹을 수 있다.
//          > 검증 부분에서 버그가 발생할 여지가 높다.
//          > 지겹다. (간지가 안난다)
//        2. 응답값에 HashMap -> 응답 클래스를 만들어주는게 좋다.
//        3. 여러개의 에러처리가 힘들다.
//        4. 세 번 이상의 반복적인 작업은 피해야 한다.
//        - 코드 && 개발에 관한 모든 것 -> 자동화 고려


//        ------------ 파라미터에 bindingresult 필요
//        if (bindingResult.hasErrors()) {
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            FieldError firstFieldError = fieldErrors.get(0);
//            String fieldName = firstFieldError.getField(); // title에 검증 오류 값이 있기 때문에 title이 넘어온다.
//            String errorMessage = firstFieldError.getDefaultMessage();// 에러메시지가 넘어간다.
//
//            Map<String, String> error = new HashMap<>();
//            error.put(fieldName, errorMessage);
//            return error; // 반환값도 맞춰줘야 한다.
//        }
//
////        return "Hello World";
        return Map.of(); // 이게 머징...?
    }
}