package com.fuxuras.patisoru.configuration;


import com.fuxuras.patisoru.dto.*;
import com.fuxuras.patisoru.dto.user.UserSummaryResponse;
import com.fuxuras.patisoru.entities.Comment;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    PostResponse postToPostResponse(Post post);

    CommentInPostResponse commentToCommentInPostResponse(Comment comment);

    FeaturedPost postToFeaturedPost(Post post);

    Post PostCreateRequestToPost(PostCreateRequest postCreateRequest);

    void updateUserFromRequest(UserEditRequest userEditRequest, @MappingTarget User user);

    Comment CommentCreateRequestToComment(CommentCreateRequest commentCreateRequest);

    // USER

    User RegisterRequestToUser(RegisterRequest registerRequest);

    UserSummaryResponse userToUserSummaryResponse(User user);

    UserResponse userToUserResponse(User user);
}
