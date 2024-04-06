package io.github.AndCandido.PassIn.dto.attendee;

public record AttendeeBadgeDTO(
    String name,
    String email,
    String checkInUrl,
    String eventId
) {
}
