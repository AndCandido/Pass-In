package io.github.AndCandido.PassIn.dto.event;

public record EventRequestDto(
    String title,
    String details,
    Integer maximumAttendees
) {
}
