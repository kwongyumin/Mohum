package com.hanghae99.finalprooject.service;

import com.hanghae99.finalprooject.dto.CommentDto;
import com.hanghae99.finalprooject.exception.ErrorCode;
import com.hanghae99.finalprooject.exception.PrivateException;
import com.hanghae99.finalprooject.model.Comment;
import com.hanghae99.finalprooject.model.User;
import com.hanghae99.finalprooject.repository.CommentRepository;
import com.hanghae99.finalprooject.repository.PostRepository;
import com.hanghae99.finalprooject.repository.UserRepository;
import com.hanghae99.finalprooject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // comment 등록
    @Transactional
    public void createComment(CommentDto.RequestDto requestDto, UserDetailsImpl userDetails) {

        convertCommentDto(commentRepository.save(
                Comment.createComment(
                        postRepository.findById(requestDto.getPostId()).orElseThrow(
                                () -> new PrivateException(ErrorCode.POST_NOT_FOUND)
                        ),
                        userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                                () -> new PrivateException(ErrorCode.NOT_FOUND_USER_INFO)
                        ),
                        requestDto.getComment()
                ))
        );
    }

    public static void convertCommentDto(Comment comment) {
        new CommentDto.ResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getUser().getProfileImg(),
                comment.getComment());
    }

    // comment 수정
    @Transactional
    public void editComment(Long commentId, CommentDto.RequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new PrivateException(ErrorCode.COMMENT_NOT_FOUND)
        );

        User user = userRepository.findByNickname(userDetails.getUser().getNickname()).orElseThrow(
                () -> new PrivateException(ErrorCode.NOT_FOUND_USER_INFO)
        );

        // 본인 comment만 수정 가능
        if (!comment.getUser().equals(user)) {
            throw new PrivateException(ErrorCode.COMMENT_UPDATE_WRONG_ACCESS);
        }

        comment.updateComment(requestDto);
    }

    // comment 삭제
    @Transactional
    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new PrivateException(ErrorCode.COMMENT_NOT_FOUND)
        );

        User user = userRepository.findByNickname(userDetails.getUser().getNickname()).orElseThrow(
                () -> new PrivateException(ErrorCode.NOT_FOUND_USER_INFO)
        );

        // 본인 comment만 삭제 가능
        if (!comment.getUser().equals(user)) {
            throw new PrivateException(ErrorCode.COMMENT_DELETE_WRONG_ACCESS);
        }
        commentRepository.deleteById(commentId);
    }
}