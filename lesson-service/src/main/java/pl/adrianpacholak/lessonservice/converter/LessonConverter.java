package pl.adrianpacholak.lessonservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.lessonservice.dto.CourseResponse;
import pl.adrianpacholak.lessonservice.dto.LessonRequest;
import pl.adrianpacholak.lessonservice.dto.LessonResponse;
import pl.adrianpacholak.lessonservice.dto.TeacherResponse;
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

    public LessonResponse lessonToLessonResponse(Lesson lesson, TeacherResponse teacher, CourseResponse course) {
        return new LessonResponse(lesson.getId(), lesson.getType(), teacher, course, lesson.getTotalStudentsSigned(),
                lesson.getLimitOfPlaces());
    }
}
