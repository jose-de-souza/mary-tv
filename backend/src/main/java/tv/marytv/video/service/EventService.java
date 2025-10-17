package tv.marytv.video.service;

import tv.marytv.video.dto.EventDto;
import tv.marytv.video.dto.EventUpsertDto;
import tv.marytv.video.entity.Event;
import tv.marytv.video.mapper.EventMapper;
import tv.marytv.video.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public EventDto createEvent(EventUpsertDto dto) {
        Event event = eventMapper.toEntity(dto);
        Event saved = eventRepository.save(event);
        return eventMapper.toDto(saved);
    }

    public EventDto updateEvent(Long id, EventUpsertDto dto) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        eventMapper.updateEntityFromDto(dto, event);
        Event updated = eventRepository.save(event);
        return eventMapper.toDto(updated);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        eventRepository.delete(event);
    }
}