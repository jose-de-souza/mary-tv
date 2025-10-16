package tv.marytv.video.dto;

public record ApiResponse<T>(
        boolean success,
        int status,
        String message,
        T data
) {}
