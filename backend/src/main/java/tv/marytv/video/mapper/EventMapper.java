package tv.marytv.video.mapper;

import tv.marytv.video.dto.EventDto;
import tv.marytv.video.dto.EventUpsertDto;
import tv.marytv.video.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDto toDto(Event event) {
        return new EventDto(event.getId(), event.getName());
    }

    public Event toEntity(EventUpsertDto dto) {
        Event event = new Event();
        updateEntityFromDto(dto, event);
        return event;
    }

    public void updateEntityFromDto(EventUpsertDto dto, Event event) {
        event.setName(dto.name());
    }
}