package com.fuxuras.patisoru.configuration;


import com.fuxuras.patisoru.dto.*;
import com.fuxuras.patisoru.dto.auth.RegisterRequest;
import com.fuxuras.patisoru.dto.comment.CommentResponse;
import com.fuxuras.patisoru.dto.post.PostCreateRequest;
import com.fuxuras.patisoru.dto.post.PostResponse;
import com.fuxuras.patisoru.dto.user.UserEditRequest;
import com.fuxuras.patisoru.dto.user.UserResponse;
import com.fuxuras.patisoru.dto.user.UserSummaryResponse;
import com.fuxuras.patisoru.entities.Comment;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    PostResponse postToPostResponse(Post post);

    CommentResponse commentToCommentResponse(Comment comment);

    Post PostCreateRequestToPost(PostCreateRequest postCreateRequest);

    void updateUserFromRequest(UserEditRequest userEditRequest, @MappingTarget User user);

    Comment CommentCreateRequestToComment(CommentCreateRequest commentCreateRequest);

    // USER

    User RegisterRequestToUser(RegisterRequest registerRequest);

    UserSummaryResponse userToUserSummaryResponse(User user);

    UserResponse userToUserResponse(User user);
}
