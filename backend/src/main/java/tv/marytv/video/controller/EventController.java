package tv.marytv.video.controller;

import tv.marytv.video.dto.ApiResponse;
import tv.marytv.video.dto.EventDto;
import tv.marytv.video.dto.EventUpsertDto;
import tv.marytv.video.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventDto>>> getAll() {
        try {
            List<EventDto> events = eventService.getAllEvents();
            return ResponseEntity.ok(new ApiResponse<>(true, 200, "All events fetched", events));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error fetching events: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDto>> getById(@PathVariable Long id) {
        try {
            return eventService.getEventById(id)
                    .map(d -> ResponseEntity.ok(new ApiResponse<>(true, 200, "Event fetched successfully", d)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, 404, "Event not found", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error fetching event: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventDto>> create(@RequestBody EventUpsertDto eventDto) {
        try {
            EventDto savedEvent = eventService.createEvent(eventDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, 201, "Event created successfully", savedEvent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error creating event: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDto>> update(@PathVariable Long id, @RequestBody EventUpsertDto eventDto) {
        try {
            return eventService.updateEvent(id, eventDto)
                    .map(d -> ResponseEntity.ok(new ApiResponse<>(true, 200, "Event updated successfully", d)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, 404, "Event not found", null)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error updating event: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            if (eventService.deleteEvent(id)) {
                return ResponseEntity.ok(new ApiResponse<>(true, 200, "Event deleted successfully", null));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, 404, "Event not found", null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error deleting event: " + e.getMessage(), null));
        }
    }
}