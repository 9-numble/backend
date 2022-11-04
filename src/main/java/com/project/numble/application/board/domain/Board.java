package com.project.numble.application.board.domain;

import com.project.numble.application.common.entity.BaseTimeEntity;
import com.project.numble.application.user.domain.Animal;
import com.project.numble.application.user.domain.AnimalType;
import com.project.numble.application.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tb_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_type_id")
    private AnimalType animal_type;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "like_count")
    private int likeCount = 0;


    @Builder
    private Board(User user, String content) {
        this.user = user;
        this.content = content;
    }

    // 이미지 없이 글만 등록
    public static Board createBoard(User user, String content) {
        Board board = new Board(user, content);
        return board;
    }

    // 임시
    // 이미지 + 글 등록
    public Board createBoardWithImages(User user, String content, Image... images) {
        Board board = new Board(user, content);

        for (Image image : images) {
            addImage(image);
        }
        return board;
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

}