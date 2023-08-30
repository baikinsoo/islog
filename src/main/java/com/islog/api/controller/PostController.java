package com.islog.api.controller;

import com.islog.api.domain.Post;
import com.islog.api.request.PostCreate;
import com.islog.api.response.PostResponse;
import com.islog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    //    Http Method
//    Get, Post, put, patch, delete, options, head, trace, connect -> 이게 다 뭐하는지 알아야 한다.
// 글 등록
//    post Method
//    @PostMapping("/posts")
//    public String post() {
//    public String post(@RequestParam String title, @RequestParam String content) {
////        log.info("title={}, content={}", title, content);
//        public String post(@RequestParam Map<String, String> params) {
//        log.info("params={}", params);
//        String title = params.get("title");
//        -> Map을 위의 두 형태처럼 만드는 것도 있고, Map 형태를 클래스로 따로 만드는 방법이 있다.
//    public Map<String, String> post(@RequestBody @Valid PostCreate params) throws Exception {
//        데이터를 검증하는 이유
//        1. client 개발자가 깜빡할 수 있다. 실수로 값을 안보낼 수 있다.
//        2. client bug로 값이 누락될 수 있다.
//        3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
//        4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
//        5. 서버 개발자의 편안함을 위해서...ㅎ
//        log.info("params={}", params.toString());

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
//        return Map.of(); // Map.of()는 Map 객체를 만드는 방식 중 하나 ()안에 아무것도 넣지 않으면 빈 Map 객체가 생성

//    ------------게시물 저장----------------- 2023.08.29.TUE

//    private final PostService postService;
//
//    @PostMapping("/posts")
//    public Map<String, String> post(@RequestBody @Valid PostCreate request) {
//        postService.write(request);
//        return Map.of();
//    }

    //    ---------반환값이 없이 생성----------------
    private final PostService postService;

    @PostMapping("/posts")
    public Post post(@RequestBody @Valid PostCreate request) {
        // POST -> 200, 201
//        Case1. 저장한 데이터 Entity -> response로 응답하기
//        Case2. 저장한 데이터의 primary_id -> response로 응답하기
//          Client에서는 수신한 id를 글 조회 API를 통해서 데이터를 수신 받음
//        Case3. 응답 필요 없음
//          -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함
        return postService.write(request);
    }

    /*
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     * */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
//    public List<PostResponse> getList(@RequestParam int page) {
//    위 코드는 결국 수동으로 만들어준 값을 받아오기 때문에 yaml에 작성한 page 보정이 적용되지 않는다.
    public List<PostResponse> getList(Pageable pageable) {
        return postService.getList(pageable);
    }
}