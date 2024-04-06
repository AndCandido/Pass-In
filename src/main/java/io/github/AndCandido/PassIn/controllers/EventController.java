package io.github.AndCandido.PassIn.controllers;

import io.github.AndCandido.PassIn.dto.attendee.AttendeeIdDTO;
import io.github.AndCandido.PassIn.dto.attendee.AttendeeRequestDTO;
import io.github.AndCandido.PassIn.dto.attendee.AttendeesListResponseDTO;
import io.github.AndCandido.PassIn.dto.event.EventIdDTO;
import io.github.AndCandido.PassIn.dto.event.EventRequestDto;
import io.github.AndCandido.PassIn.dto.event.EventResponseDTO;
import io.github.AndCandido.PassIn.services.AttendeeService;
import io.github.AndCandido.PassIn.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventDetails(@PathVariable String id) {
        EventResponseDTO eventDetails = eventService.getEventDetails(id);
        return ResponseEntity.ok(eventDetails);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id) {
        AttendeesListResponseDTO eventAttendees = attendeeService.getEventAttendees(id);
        return ResponseEntity.ok(eventAttendees);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> saveEvent(
        @RequestBody EventRequestDto eventRequestDto,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        EventIdDTO eventDTO = eventService.saveEvent(eventRequestDto);

        URI uri = uriComponentsBuilder
            .path("/events/{id}")
            .buildAndExpand(eventDTO.eventId())
            .toUri();

        return ResponseEntity.created(uri).body(eventDTO);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerAttendee(
        @PathVariable String eventId,
        @RequestBody AttendeeRequestDTO attendeeRequestDTO,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        AttendeeIdDTO attendeeIdDTO = eventService.registerAttendeeOnEvent(attendeeRequestDTO, eventId);

        URI uri = uriComponentsBuilder
            .path("/attendees/{attendeeId}/badge")
            .buildAndExpand(attendeeIdDTO.attendeeId())
            .toUri();

        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }
}
