package tv.marytv.video.dto;

public record UserUpsertDto(
        String username,
        String password,
        String role
) {}
