package io.github.AndCandido.PassIn.dto.attendee;

import java.util.List;

public record AttendeesListResponseDTO(
    List<AttendeeDetails> attendeeDetails
) {
}
