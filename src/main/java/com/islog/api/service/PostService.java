package com.islog.api.service;

import com.islog.api.domain.Post;
import com.islog.api.repository.PostRepository;
import com.islog.api.request.PostCreate;
import com.islog.api.request.PostSearch;
import com.islog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



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
    
    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
//        최초에 Optional을 감싸서 반환한다 -> 값이 없을 경우 null을 반환하기 때문이다.
//        글이 없으면 없다는 적당한 에러를 만드는 것이 좋다.

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    // 글이 -> 100,000,000 -> DB 글 모두 조회하는 경우 -> DB가 다운 될 수 있다.
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래픽 비용 등이 많이 발생할 수 있다.



////    public List<PostResponse> getList(int page) {
//    public List<PostResponse> getList(Pageable pageable) {
//        // web -> page 1 -> 0 웹에서 1이 날라왔을 때 0으로 변환한다.
//
//
////        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));
//        // 수동 작업을 한 경우 -> PageDefault 설정을 하면 위의 코드는 필요 없다.
//
//        return postRepository.findAll(pageable).stream()
////                .map(post -> PostResponse.builder()
////                        .id(post.getId())
////                        .title(post.getTitle())
////                        .content(post.getContent())
////                        .build())
//                // -> 추후에 반복 작업이 될 확률이 높기 때문에 좋지 않다.
//
//                .map(post -> new PostResponse(post))
////                .map(PostResponse::new)
//                //위의 두개의 코드가 같은 코드다... 놀랍다...
//
//                //생성자 주입 방식을 생성해줬기 때문에 코드가 간단해진다.
//                .collect(Collectors.toList());
//    }

    // -------------------------------------
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(post -> new PostResponse(post))
////                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}