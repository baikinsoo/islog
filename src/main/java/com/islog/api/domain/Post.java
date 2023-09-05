package com.islog.api.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob // Java에서는 String이지만 DB에 저장될 때는 Long text로 저장되게 하기 위한 어노테이션이다.
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    public void change(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
    //위의 경우 파라미터 순서가 변경되면 버그를 찾기 매우 힘들다. 이를 대비해 아래와 같이 사용한다.

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    // Builder를 넘기는 이유는 build()를 쓰게되면 객체를 생성하며 데이터를 확정시키기 때문이다.
    // 데이터를 확정 시키지 않고 넘기기 위해 Builder 형태로 전달한다.

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}