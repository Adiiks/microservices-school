package pl.adrianpacholak.lessonservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.lessonservice.dto.LessonRequest;
import pl.adrianpacholak.lessonservice.model.Lesson;

@Component
public class LessonConverter {

    public Lesson lessonRequestToLesson(LessonRequest request) {
        return Lesson.builder()
                .courseId(request.courseId())
                .term(request.term())
                .status(request.status())
                .type(request.type())
                .day(request.day())
                .beginTime(request.beginTime())
                .endTime(request.endTime())
                .classroom(request.classroom())
                .limitOfPlaces(request.limitOfPlaces())
                .teacherId(request.teacherId())
                .totalStudentsSigned(0)
                .build();
    }
}
