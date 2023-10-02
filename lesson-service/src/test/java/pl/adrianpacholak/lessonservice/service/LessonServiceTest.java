package pl.adrianpacholak.lessonservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.lessonservice.client.CourseClient;
import pl.adrianpacholak.lessonservice.client.UserClient;
import pl.adrianpacholak.lessonservice.converter.LessonConverter;
import pl.adrianpacholak.lessonservice.dto.LessonRequest;
import pl.adrianpacholak.lessonservice.model.EStatus;
import pl.adrianpacholak.lessonservice.model.ETerm;
import pl.adrianpacholak.lessonservice.model.EType;
import pl.adrianpacholak.lessonservice.model.Lesson;
import pl.adrianpacholak.lessonservice.repository.LessonRepository;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    private LessonService lessonService;

    @Mock
    private CourseClient courseClient;

    @Mock
    private UserClient userClient;

    @Mock
    private LessonRepository lessonRepository;

    private LessonConverter lessonConverter = new LessonConverter();

    @Captor
    private ArgumentCaptor<Lesson> lessonAc;

    @BeforeEach
    void setUp() {
        lessonService = new LessonService(courseClient, userClient, lessonConverter, lessonRepository);
    }

    @DisplayName("Create new lesson - Failed: begin time is after end time")
    @Test
    void createLesson_FailedInvalidTimes() {
        LessonRequest request = new LessonRequest(1, ETerm.SUMMER, EStatus.ENDED, EType.LABORATORY, DayOfWeek.FRIDAY,
                "08:15", "07:45", 112, 30, 1);

        assertThrows(ResponseStatusException.class, () -> lessonService.createLesson(request));
    }

    @DisplayName("Create new lesson - Failed: course not exists")
    @Test
    void createLesson_FailedInvalidCourse() {
        LessonRequest request = new LessonRequest(1, ETerm.SUMMER, EStatus.ENDED, EType.LABORATORY, DayOfWeek.FRIDAY,
                "08:15", "09:45", 112, 30, 1);

        Map<String, Boolean> courseExists = Collections.singletonMap("exists", false);

        when(courseClient.checkCourseExists(anyInt())).thenReturn(courseExists);

        assertThrows(ResponseStatusException.class, () -> lessonService.createLesson(request));
    }

    @DisplayName("Create new lesson - Failed: teacher not exists")
    @Test
    void createLesson_FailedInvalidTeacher() {
        LessonRequest request = new LessonRequest(1, ETerm.SUMMER, EStatus.ENDED, EType.LABORATORY, DayOfWeek.FRIDAY,
                "08:15", "09:45", 112, 30, 1);

        Map<String, Boolean> courseExists = Collections.singletonMap("exists", true);
        Map<String, Boolean> teacherExists = Collections.singletonMap("exists", false);

        when(courseClient.checkCourseExists(anyInt())).thenReturn(courseExists);
        when(userClient.checkTeacherExists(anyInt())).thenReturn(teacherExists);

        assertThrows(ResponseStatusException.class, () -> lessonService.createLesson(request));
    }

    @DisplayName("Create new lesson")
    @Test
    void createLesson() {
        LessonRequest request = new LessonRequest(1, ETerm.SUMMER, EStatus.ENDED, EType.LABORATORY, DayOfWeek.FRIDAY,
                "08:15", "09:45", 112, 30, 1);

        Map<String, Boolean> courseExists = Collections.singletonMap("exists", true);
        Map<String, Boolean> teacherExists = Collections.singletonMap("exists", true);

        when(courseClient.checkCourseExists(anyInt())).thenReturn(courseExists);
        when(userClient.checkTeacherExists(anyInt())).thenReturn(teacherExists);

        lessonService.createLesson(request);

        verify(lessonRepository).save(lessonAc.capture());
        Lesson newLesson = lessonAc.getValue();

        assertNull(newLesson.getId());
        assertEquals(request.courseId(), newLesson.getCourseId());
        assertEquals(request.term(), newLesson.getTerm());
        assertEquals(request.status(), newLesson.getStatus());
        assertEquals(request.type(), newLesson.getType());
        assertEquals(request.day(), newLesson.getDay());
        assertEquals(request.beginTime(), newLesson.getBeginTime());
        assertEquals(request.endTime(), newLesson.getEndTime());
        assertEquals(request.classroom(), newLesson.getClassroom());
        assertEquals(0, newLesson.getTotalStudentsSigned());
        assertEquals(request.limitOfPlaces(), newLesson.getLimitOfPlaces());
        assertEquals(request.teacherId(), newLesson.getTeacherId());
    }
}