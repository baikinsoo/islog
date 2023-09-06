package com.islog.api.service;

import com.islog.api.domain.Post;
import com.islog.api.domain.PostEditor;
import com.islog.api.exception.PostNotFound;
import com.islog.api.repository.PostRepository;
import com.islog.api.request.PostCreate;
import com.islog.api.request.PostEdit;
import com.islog.api.request.PostSearch;
import com.islog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
                .orElseThrow(PostNotFound::new);
        // 나중에 메시지를 검증 할 필요가 없다.
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
//                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    //--------게시글 수정
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

//        post.setTitle(postEdit.getTitle());
//        post.setContent(postEdit.getContent());

//        post.change(postEdit.getTitle(), postEdit.getContent());

        // 내부적으로 setter에 의해 입력 값이 저장되었을 것이지만 save를 해주지 않으면 DB에 저장되지 않기 때문에
        // 호출 시 변경된 값을 확인할 수 없다.
        // or @Transactional 어노테이션을 붙여준다.

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);

        // 아래 방법은 수정하지 않은 데이터의 경우 기존의 데이터를 남기는 방식이다.
//        if (postEdit.getTitle() != null) {
//            editorBuilder.title(postEdit.getTitle());
//
//        }
//        if (postEdit.getContent() != null) {
//            editorBuilder.content(postEdit.getContent());
//        }
//
//        post.edit(editorBuilder.build());

        // 1. setter를 이용한 방법(setter는 권장하지 않음), 2. 메서드를 이용한 방법(파라미터 순서가 바뀌면 곤란)
        // 3. builder를 이용한 방법(제일 안전하긴 하다)
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        // -> 존재하는 경우
        postRepository.delete(post);
    }
}