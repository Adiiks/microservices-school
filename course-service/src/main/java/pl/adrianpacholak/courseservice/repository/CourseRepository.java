package pl.adrianpacholak.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.courseservice.model.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findAllByIdIn(List<Integer> ids);
}
