package com.project.numble.application.like.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.like.domain.Like;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.like.repository.LikeRepository;
import com.project.numble.application.like.service.exception.AlreadyLikeBoardException;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.like.service.exception.LikeNotExistsException;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StandardLikeService implements LikeService{

    private final LikeRepository likeRepository;

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    @Override
    public void addLike(Long userId, Long boardId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotExistsException());

        Like like = new Like(user, board);
        if (isNotAlreadyLike(userId, boardId)) {
            likeRepository.save(like);
            board.addLike(like);
            board.plusLikeCount();
            return;
        }

        throw new AlreadyLikeBoardException();
    }

    @Override
    public void cancelLike(Long userId, Long boardId) {
        Like like = likeRepository.findByUserIdAndBoardId(userId, boardId).orElseThrow(() -> new LikeNotExistsException());
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotExistsException());
        likeRepository.delete(like);
        board.minusLikeCount();
        board.delLike(like);
    }


    @Override
    public boolean isNotAlreadyLike(Long userId, Long boardId) {
        return likeRepository.findByUserIdAndBoardId(userId, boardId).isEmpty();
    }
}
