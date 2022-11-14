package com.project.numble.application.board.domain;

import com.project.numble.application.common.entity.BaseTimeEntity;
import com.project.numble.application.user.domain.Animal;
import com.project.numble.application.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Table(name = "tb_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",updatable = false) // 읽기 전용 insertable = false
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BoardAnimal> boardAnimals = new HashSet<>();

    @Column(nullable = false)
    private String boardAddress;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String categoryType;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "like_count")
    private int likeCount = 0;


    @Builder
    private Board(User user, String content, String boardAddress, List<Image> images, String categoryType, Animal animal) {
        this.user = user;
        this.content = content;
        this.boardAddress = boardAddress;
        this.images = images;
        //this.animals = animal;
        this.categoryType = categoryType;
    }


    // 이미지 등록
    public void addImage(Image image) {
        this.images.add(image);
    }

    // 글만 수정
    public void update(String content) {
        this.content = content;
    }

    // 조회수 + 1
    public int addViewCount(Board board) {
        return board.getViewCount() + 1;
    }
    // 좋아요 + 1
    public int addLikeCount(Board board) {
        return board.getLikeCount() + 1;
    }

    // 댓글 추가
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.initBoard(this);
    }

    // 댓글 삭제
    public void delComment(Comment comment) {
        this.comments.add(comment);
    }

    public void initUser(User user) {
        if (this.user == null) {
            this.user = user;
        }
    }
}
