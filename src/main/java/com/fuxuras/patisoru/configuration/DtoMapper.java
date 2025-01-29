package com.fuxuras.patisoru.configuration;


import com.fuxuras.patisoru.dto.*;
import com.fuxuras.patisoru.entities.Comment;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    User RegisterRequestToUser(RegisterRequest registerRequest);

    PostResponse postToPostResponse(Post post);

    UserInPostResponse userToUserInPostResponse(User user);

    CommentInPostResponse commentToCommentInPostResponse(Comment comment);

    FeaturedPost postToFeaturedPost(Post post);

    UserInFeaturedPost userToUserInFeaturedPost(User user);

    Post PostCreateRequestToPost(PostCreateRequest postCreateRequest);

    UserResponse userToUserResponse(User user);


    void updateUserFromRequest(UserEditRequest userEditRequest, @MappingTarget User user);
}
