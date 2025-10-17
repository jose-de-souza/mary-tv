package tv.marytv.video.dto;

import java.util.Date;

public record ItemUpsertDto(
        String title,
        String description,
        String iconUrl,
        String videoUrl,
        Date itemDate,
        boolean isNew,
        boolean isHeadline,
        String contentType,
        Long parentId,
        Long categoryId,
        Long eventId
) {}