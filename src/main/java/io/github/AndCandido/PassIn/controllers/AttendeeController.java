package io.github.AndCandido.PassIn.controllers;

import io.github.AndCandido.PassIn.dto.attendee.AttendeeBadgeResponseDTO;
import io.github.AndCandido.PassIn.dto.attendee.AttendeeRequestDTO;
import io.github.AndCandido.PassIn.services.AttendeeService;
import io.github.AndCandido.PassIn.services.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private final AttendeeService attendeeService;

    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(
        @PathVariable String attendeeId,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        AttendeeBadgeResponseDTO attendeeBadgeResponseDTO =
            attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder);
        return ResponseEntity.ok(attendeeBadgeResponseDTO);
    }

    @PostMapping("/{attendeeId}/check-in")
    public ResponseEntity<Void> checkInAttendee(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        attendeeService.checkInAttendee(attendeeId);

        URI uri = uriComponentsBuilder
            .path("/attendees/{attendeeId}/badge")
            .buildAndExpand(attendeeId)
            .toUri();

        return ResponseEntity.created(uri).build();
    }
}
