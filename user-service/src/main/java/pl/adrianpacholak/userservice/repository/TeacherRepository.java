package pl.adrianpacholak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.userservice.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
