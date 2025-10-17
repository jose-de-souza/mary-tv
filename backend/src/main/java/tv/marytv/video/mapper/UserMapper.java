package tv.marytv.video.mapper;

import tv.marytv.video.dto.UserDto;
import tv.marytv.video.dto.UserUpsertDto;
import tv.marytv.video.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getRole());
    }

    public User toEntity(UserUpsertDto dto) {
        User user = new User();
        updateEntityFromDto(dto, user);
        return user;
    }

    public void updateEntityFromDto(UserUpsertDto dto, User user) {
        user.setUsername(dto.username());
        user.setRole(dto.role() != null ? dto.role() : user.getRole());
        // Password handled in service
    }
}