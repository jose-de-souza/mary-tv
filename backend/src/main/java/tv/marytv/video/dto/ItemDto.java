package tv.marytv.video.dto;

import java.util.Date;
import java.util.List;

public record ItemDto(
        Long id,
        String title,
        String description,
        String iconUrl,
        String videoUrl,
        Date itemDate,
        boolean isNew,
        boolean isHeadline,
        Long parentId,
        Long categoryId,
        Long eventId,
        List<ItemDto> children
) {}