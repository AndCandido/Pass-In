package io.github.AndCandido.PassIn.dto.attendee;

import lombok.Builder;

@Builder
public record AttendeeBadgeDTO(
    String name,
    String email,
    String checkInUrl,
    String eventId
) {
}
