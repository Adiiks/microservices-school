package pl.adrianpacholak.courseservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.courseservice.client.FacultyClient;
import pl.adrianpacholak.courseservice.converter.CourseConverter;
import pl.adrianpacholak.courseservice.dto.CourseRequest;
import pl.adrianpacholak.courseservice.dto.CourseResponse;
import pl.adrianpacholak.courseservice.model.Course;
import pl.adrianpacholak.courseservice.model.ELanguage;
import pl.adrianpacholak.courseservice.repository.CourseRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private FacultyClient facultyClient;

    private CourseConverter courseConverter = new CourseConverter();

    @Captor
    private ArgumentCaptor<Course> courseAc;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(courseRepository, facultyClient, courseConverter);
    }

    @DisplayName("Create new course - faculty not exists")
    @Test
    void createCourse_FacultyNotFound() {
        CourseRequest request = buildCourseRequest();

        when(facultyClient.checkFacultyExists(anyInt()))
                .thenReturn(Collections.singletonMap("exists", false));

        assertThrows(ResponseStatusException.class, () -> courseService.createCourse(request));
    }

    @DisplayName("Create new course")
    @Test
    void createCourse() {
        CourseRequest request = buildCourseRequest();

        when(facultyClient.checkFacultyExists(anyInt()))
                .thenReturn(Collections.singletonMap("exists", true));

        courseService.createCourse(request);

        verify(courseRepository).save(courseAc.capture());

        Course course = courseAc.getValue();
        assertEquals(request.ectsPoints(), course.getEctsPoints());
        assertEquals(request.facultyId(), course.getFacultyId());
        assertEquals(request.language(), course.getLanguage());
        assertEquals(request.name(), course.getName());
        assertNull(course.getId());
    }

    @DisplayName("Get courses based on list of IDs")
    @Test
    void getCoursesByIds() {
        Course course = Course.builder().id(1).name("Programming").build();

        when(courseRepository.findAllByIdIn(anyList())).thenReturn(List.of(course));

        List<CourseResponse> courses = courseService.getCoursesByIds(List.of(1));

        assertEquals(1, courses.size());
    }

    private CourseRequest buildCourseRequest() {
        return new CourseRequest("Programming", 1, BigDecimal.valueOf(3.00), ELanguage.PL);
    }
}