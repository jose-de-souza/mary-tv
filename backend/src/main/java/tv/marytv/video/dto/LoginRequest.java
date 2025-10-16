package tv.marytv.video.dto;

public record LoginRequest(
        String username,
        String password
) {}