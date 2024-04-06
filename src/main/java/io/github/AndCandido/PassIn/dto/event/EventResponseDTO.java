package io.github.AndCandido.PassIn.dto.event;

import io.github.AndCandido.PassIn.domain.event.Event;
import lombok.Getter;

@Getter
public class EventResponseDTO {

    private final EventDetailsDTO eventDetails;

    public EventResponseDTO(Event event, int attendeesAmount) {
        eventDetails = EventDetailsDTO.builder()
            .id(event.getId())
            .title(event.getTitle())
            .details(event.getDetails())
            .slug(event.getSlug())
            .maximumAttendees(event.getMaximumAttendees())
            .attendeesAmount(attendeesAmount)
            .build();
    }
}
