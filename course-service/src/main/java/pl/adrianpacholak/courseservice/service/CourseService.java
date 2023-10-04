package pl.adrianpacholak.courseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.courseservice.client.FacultyClient;
import pl.adrianpacholak.courseservice.converter.CourseConverter;
import pl.adrianpacholak.courseservice.dto.CourseRequest;
import pl.adrianpacholak.courseservice.dto.CourseResponse;
import pl.adrianpacholak.courseservice.model.Course;
import pl.adrianpacholak.courseservice.repository.CourseRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final FacultyClient facultyClient;
    private final CourseConverter courseConverter;

    @Transactional
    public void createCourse(CourseRequest request) {
        Map<String, Boolean> response = facultyClient.checkFacultyExists(request.facultyId());

        if (!response.get("exists")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Faculty not exists");
        }

        Course newCourse = courseConverter.courseRequestToCourse(request);
        courseRepository.save(newCourse);
    }

    public boolean checkCourseExists(Integer courseId) {
        return courseRepository.existsById(courseId);
    }

    public List<CourseResponse> getCoursesByIds(List<Integer> ids) {
        return courseRepository.findAllByIdIn(ids)
                .stream()
                .map(courseConverter::courseToCourseResponse)
                .toList();
    }
}
