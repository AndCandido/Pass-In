package io.github.AndCandido.PassIn.services;

import io.github.AndCandido.PassIn.domain.attendee.Attendee;
import io.github.AndCandido.PassIn.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import io.github.AndCandido.PassIn.domain.event.Event;
import io.github.AndCandido.PassIn.domain.event.exceptions.EventFullAttendeesException;
import io.github.AndCandido.PassIn.dto.attendee.AttendeeIdDTO;
import io.github.AndCandido.PassIn.dto.attendee.AttendeeRequestDTO;
import io.github.AndCandido.PassIn.dto.event.EventIdDTO;
import io.github.AndCandido.PassIn.dto.event.EventRequestDto;
import io.github.AndCandido.PassIn.dto.event.EventResponseDTO;
import io.github.AndCandido.PassIn.domain.event.exceptions.EventNotFoundException;
import io.github.AndCandido.PassIn.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    public final EventRepository eventRepository;
    public final AttendeeService attendeeService;

    public EventResponseDTO getEventDetails(String eventId) {
        Event eventFounded = getEventById(eventId);
        List<Attendee> attendees = attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(eventFounded, attendees.size());
    }

    public Event getEventById(String eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
    }

    public EventIdDTO saveEvent(EventRequestDto eventRequestDto) {
        Event event = new Event();
        BeanUtils.copyProperties(eventRequestDto, event);
        event.setSlug(createEventSlug(eventRequestDto.title()));

        eventRepository.save(event);

        return new EventIdDTO(event.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(AttendeeRequestDTO attendeeRequestDTO, String eventId) {
        boolean isAttendeeRegistered = attendeeService.isAttendeeAlreadyRegisteredOnEvent(attendeeRequestDTO.email(), eventId);
        if(isAttendeeRegistered)
            throw new AttendeeAlreadyRegisteredException("Attendee already registered on event");

        Event event = getEventById(eventId);
        List<Attendee> attendees = attendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendees.size()) {
            throw new EventFullAttendeesException("Event is full");
        }

        Attendee attendee = new Attendee();
        BeanUtils.copyProperties(attendeeRequestDTO, attendee);
        attendee.setEvent(event);
        attendee.setCreateAt(LocalDateTime.now());

        attendeeService.saveAttendee(attendee);

        return new AttendeeIdDTO(attendee.getId());
    }


    private String createEventSlug(String text) {
        String textNormalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return textNormalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
            .replaceAll("[^\\w\\s]", "")
            .replaceAll("\\s+", "-")
            .toLowerCase();
    }
}
