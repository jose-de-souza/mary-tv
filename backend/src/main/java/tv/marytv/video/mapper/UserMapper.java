package tv.marytv.video.mapper;

import tv.marytv.video.dto.UserDto;
import tv.marytv.video.dto.UserUpsertDto;
import tv.marytv.video.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(UserUpsertDto userUpsertDto);
}