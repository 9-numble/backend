package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndBoardId(Long userId, Long boardId);
}
