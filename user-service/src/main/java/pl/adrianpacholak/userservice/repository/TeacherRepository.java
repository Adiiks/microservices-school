package pl.adrianpacholak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.userservice.model.Teacher;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    List<Teacher> findAllByIdIn(List<Integer> ids);
}
