package pl.adrianpacholak.lessonservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.lessonservice.client.CourseClient;
import pl.adrianpacholak.lessonservice.client.UserClient;
import pl.adrianpacholak.lessonservice.converter.LessonConverter;
import pl.adrianpacholak.lessonservice.dto.LessonRequest;
import pl.adrianpacholak.lessonservice.model.Lesson;
import pl.adrianpacholak.lessonservice.repository.LessonRepository;

import java.time.LocalTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final CourseClient courseClient;
    private final UserClient userClient;
    private final LessonConverter lessonConverter;
    private final LessonRepository lessonRepository;

    @Transactional
    public void createLesson(LessonRequest request) {
        validateLessonRequest(request);

        Lesson newLesson = lessonConverter.lessonRequestToLesson(request);

        lessonRepository.save(newLesson);
    }

    private void validateLessonRequest(LessonRequest request) {
        if (!validateLessonTime(request.beginTime(), request.endTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Begin time cannot be after end time.");
        }

        if (!checkCourseExists(request.courseId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Course not exists.");
        }

        if (!checkTeacherExists(request.teacherId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Teacher not exists.");
        }
    }

    private boolean validateLessonTime(String beginTime, String endTime) {
        LocalTime start = LocalTime.parse(beginTime);
        LocalTime end = LocalTime.parse(endTime);

        return start.isBefore(end);
    }

    private boolean checkCourseExists(Integer courseId) {
        Map<String, Boolean> courseExists = courseClient.checkCourseExists(courseId);

        return courseExists.get("exists");
    }

    private boolean checkTeacherExists(Integer teacherId) {
        Map<String, Boolean> teacherExists = userClient.checkTeacherExists(teacherId);

        return teacherExists.get("exists");
    }

    public boolean checkLessonExists(Integer lessonId) {
        return lessonRepository.existsById(lessonId);
    }
}
