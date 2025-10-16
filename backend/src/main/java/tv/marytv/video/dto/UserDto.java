package tv.marytv.video.dto;

public record UserDto(
        Long id,
        String username,
        String role
) {}