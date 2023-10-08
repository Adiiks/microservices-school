package pl.adrianpacholak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.userservice.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByPesel(String pesel);

    List<Student> findAllByIdIn(List<Integer> ids);
}
