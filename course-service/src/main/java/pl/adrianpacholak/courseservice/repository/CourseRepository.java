package pl.adrianpacholak.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.courseservice.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
