package pl.adrianpacholak.lessonservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.lessonservice.model.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
}
