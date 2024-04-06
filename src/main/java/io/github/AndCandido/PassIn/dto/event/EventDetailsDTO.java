package io.github.AndCandido.PassIn.dto.event;

import lombok.Builder;

@Builder
public record EventDetailsDTO(
    String id,
    String title,
    String details,
    String slug,
    Integer maximumAttendees,
    Integer attendeesAmount
) {
}
