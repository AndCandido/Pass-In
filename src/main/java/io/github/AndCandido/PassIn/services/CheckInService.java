package io.github.AndCandido.PassIn.services;

import io.github.AndCandido.PassIn.domain.attendee.Attendee;
import io.github.AndCandido.PassIn.domain.checkin.CheckIn;
import io.github.AndCandido.PassIn.domain.checkin.exceptions.CheckInAlreadyExistException;
import io.github.AndCandido.PassIn.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;

    public void checkInAttendee(Attendee attendee) {
        CheckIn checkInFound = getCheckInByAttendeeId(attendee.getId());
        if(checkInFound != null) {
            throw new CheckInAlreadyExistException("Attendee already checked in");
        }

        CheckIn checkIn = CheckIn.builder()
            .attendee(attendee)
            .createAt(LocalDateTime.now())
            .build();

        checkInRepository.save(checkIn);
    }


    public CheckIn getCheckInByAttendeeId(String attendeeId) {
        return checkInRepository.findByAttendeeId(attendeeId)
            .orElse(null);
    }
}
