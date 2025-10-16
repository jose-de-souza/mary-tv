package tv.marytv.video.mapper;

import tv.marytv.video.dto.EventDto;
import tv.marytv.video.dto.EventUpsertDto;
import tv.marytv.video.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDto toDto(Event event);

    List<EventDto> toDtoList(List<Event> events);

    @Mapping(target = "id", ignore = true)
    Event toEntity(EventUpsertDto eventUpsertDto);
}