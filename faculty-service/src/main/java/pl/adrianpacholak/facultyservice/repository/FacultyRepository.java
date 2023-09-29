package pl.adrianpacholak.facultyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.facultyservice.model.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    boolean existsByName(String name);

    List<Faculty> findByIdIn(List<Integer> facultiesIds);

    Optional<Faculty> findByName(String facultyName);
}
