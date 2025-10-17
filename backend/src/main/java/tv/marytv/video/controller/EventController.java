package tv.marytv.video.controller;

import tv.marytv.video.dto.ApiResponse;
import tv.marytv.video.dto.EventDto;
import tv.marytv.video.dto.EventUpsertDto;
import tv.marytv.video.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventDto>>> getAllEvents() {
        List<EventDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Events retrieved", events));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EventDto>> createEvent(@RequestBody EventUpsertDto dto) {
        EventDto event = eventService.createEvent(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, 201, "Event created", event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EventDto>> updateEvent(@PathVariable Long id, @RequestBody EventUpsertDto dto) {
        EventDto updated = eventService.updateEvent(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Event updated", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Event deleted", null));
    }
}