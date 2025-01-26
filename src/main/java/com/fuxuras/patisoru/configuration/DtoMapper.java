package com.fuxuras.patisoru.configuration;


import com.fuxuras.patisoru.dto.RegisterRequest;
import com.fuxuras.patisoru.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper( DtoMapper.class );

    User RegisterRequestToUser(RegisterRequest registerRequest);
}
