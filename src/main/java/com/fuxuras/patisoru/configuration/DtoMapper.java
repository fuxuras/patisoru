package com.fuxuras.patisoru.configuration;


import com.fuxuras.patisoru.dto.CommentInPostResponse;
import com.fuxuras.patisoru.dto.PostResponse;
import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.dto.UserInPostResponse;
import com.fuxuras.patisoru.entities.Comment;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    User RegisterRequestToUser(RegisterRequest registerRequest);

    PostResponse postToPostResponse(Post post);

    UserInPostResponse userToUserInPostResponse(User user);

    CommentInPostResponse commentToCommentInPostResponse(Comment comment);
}
