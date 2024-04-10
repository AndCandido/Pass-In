package io.github.AndCandido.PassIn.services;

import io.github.AndCandido.PassIn.domain.attendee.Attendee;
import io.github.AndCandido.PassIn.domain.attendee.exceptions.AttendeeNotFoundExceptions;
import io.github.AndCandido.PassIn.domain.checkin.CheckIn;
import io.github.AndCandido.PassIn.dto.attendee.AttendeeBadgeResponseDTO;
import io.github.AndCandido.PassIn.dto.attendee.AttendeeDetails;
import io.github.AndCandido.PassIn.dto.attendee.AttendeesListResponseDTO;
import io.github.AndCandido.PassIn.dto.attendee.AttendeeBadgeDTO;
import io.github.AndCandido.PassIn.repositories.AttendeeRepository;
import io.github.AndCandido.PassIn.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public Attendee saveAttendee(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventAttendees(String eventId) {
        List<Attendee> attendeesFromEvent = getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetails = attendeesFromEvent.stream().map(attendee -> {
                CheckIn checkIn = checkInService.getCheckInByAttendeeId(attendee.getId());

                LocalDateTime checkedInAt = checkIn != null ? checkIn.getCreateAt() : null;

                return new AttendeeDetails(
                    attendee.getId(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendee.getCreateAt(),
                    checkedInAt
                );
            })
            .toList();


        return new AttendeesListResponseDTO(attendeeDetails);
    }

    public boolean isAttendeeAlreadyRegisteredOnEvent(String email, String eventId) {
        Optional<Attendee> attendeeFound = attendeeRepository.findByEventIdAndEmail(eventId, email);
        return attendeeFound.isPresent();   
    }

    public Attendee getAttendeeById(String attendeeId) {
        return attendeeRepository.findById(attendeeId)
            .orElseThrow(() -> new AttendeeNotFoundExceptions("Attendee not found with ID: " + attendeeId));
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attendee = getAttendeeById(attendeeId);

        URI uri = uriComponentsBuilder
            .path("/attendee/{attendeeId}/check-in")
            .buildAndExpand(attendeeId)
            .toUri();

        AttendeeBadgeDTO attendeeBadgeDTO = AttendeeBadgeDTO.builder()
            .name(attendee.getName())
            .email(attendee.getEmail())
            .checkInUrl(uri.toString())
            .eventId(attendee.getEvent().getId())
            .build();

        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }

    public void checkInAttendee(String attendeeId) {
        Attendee attendeeFound = getAttendeeById(attendeeId);
        checkInService.checkInAttendee(attendeeFound);
    }
}
