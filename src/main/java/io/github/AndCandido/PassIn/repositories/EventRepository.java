package io.github.AndCandido.PassIn.repositories;

import io.github.AndCandido.PassIn.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
}
