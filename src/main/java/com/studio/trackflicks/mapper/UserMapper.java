package com.studio.trackflicks.mapper;

import com.studio.trackflicks.dto.auth.RegisterRequest;
import com.studio.trackflicks.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "username", target = "firstName")
    User convertToUser(RegisterRequest registerRequest);
}
