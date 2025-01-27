package com.fuxuras.patisoru.configuration;


import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    User RegisterRequestToUser(RegisterRequest registerRequest);
}
