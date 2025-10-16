package tv.marytv.video.service;

import tv.marytv.video.dto.EventDto;
import tv.marytv.video.dto.EventUpsertDto;
import tv.marytv.video.entity.Event;
import tv.marytv.video.mapper.EventMapper;
import tv.marytv.video.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Transactional(readOnly = true)
    public List<EventDto> getAllEvents() {
        return eventMapper.toDtoList(eventRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Optional<EventDto> getEventById(Long id) {
        return eventRepository.findById(id).map(eventMapper::toDto);
    }

    @Transactional
    public EventDto createEvent(EventUpsertDto eventDto) {
        if (eventDto.name() == null || eventDto.name().isEmpty()) {
            throw new IllegalArgumentException("Event name is required");
        }
        Event event = eventMapper.toEntity(eventDto);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    @Transactional
    public Optional<EventDto> updateEvent(Long id, EventUpsertDto eventDto) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    if (eventDto.name() != null && !eventDto.name().isEmpty()) {
                        existingEvent.setName(eventDto.name());
                    }
                    Event updatedEvent = eventRepository.save(existingEvent);
                    return eventMapper.toDto(updatedEvent);
                });
    }

    @Transactional
    public boolean deleteEvent(Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}