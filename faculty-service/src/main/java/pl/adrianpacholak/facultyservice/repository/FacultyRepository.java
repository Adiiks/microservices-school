package pl.adrianpacholak.facultyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.facultyservice.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    boolean existsByName(String name);

    List<Faculty> findByIdIn(List<Integer> facultiesIds);
}
