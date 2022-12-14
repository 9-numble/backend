package com.project.numble.application.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.numble.application.board.domain.Board;
import com.project.numble.application.image.domain.Image;
import com.project.numble.application.user.domain.enums.AnimalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "boardId")
public class GetAllBoardResponse {

    private Long boardId;
    private String content;

    // 이미지 예정
    private List<Long> imageIds;

    private String nickname;

    // 동물 예정
    private List<String> boardAnimalTypes;

    private String categoryType;

    private String boardAddress;

    private Integer likeCount;

    @Setter
    private boolean likeCheck;

    private Integer viewCount;

    private Integer bookmarkCount;

    @Setter
    private boolean bookmarkCheck;

    @Setter
    private Long lastBoardId;

    // 생성 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    // 수정 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private  LocalDateTime lastModifiedDate;

    public GetAllBoardResponse(Board board) {
        this.boardId = board.getId();
        this.content = board.getContent();
        this.nickname = board.getUser().getNickname();
        this.boardAddress = board.getBoardAddress();
        this.categoryType = board.getCategoryType();
        this.boardAnimalTypes = board.getBoardAnimals().stream().map(animal -> AnimalType.getName(animal.getAnimalType())).collect(
                Collectors.toList());
        this.imageIds = board.getImages().stream().map(image -> image.getId()).collect(Collectors.toList());
        this.likeCount = board.getLikeCount();
        this.viewCount = board.getViewCount();
        this.bookmarkCount = board.getBookmarkCount();
        this.createdDate = board.getCreatedDate();
        this.lastModifiedDate = board.getLastModifiedDate();
    }

}
