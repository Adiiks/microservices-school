package pl.adrianpacholak.lessonservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.lessonservice.model.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
}
