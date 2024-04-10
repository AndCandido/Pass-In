package io.github.AndCandido.PassIn.domain.checkin;

import io.github.AndCandido.PassIn.domain.attendee.Attendee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_ins")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "attendee_id", nullable = false)
    private Attendee attendee;

    @Column(name = "created_at")
    private LocalDateTime createAt;
}
