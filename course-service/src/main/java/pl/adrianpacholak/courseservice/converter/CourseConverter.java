package pl.adrianpacholak.courseservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.courseservice.dto.CourseRequest;
import pl.adrianpacholak.courseservice.dto.CourseResponse;
import pl.adrianpacholak.courseservice.model.Course;

@Component
public class CourseConverter {

    public Course courseRequestToCourse(CourseRequest courseRequest) {
        return Course.builder()
                .ectsPoints(courseRequest.ectsPoints())
                .facultyId(courseRequest.facultyId())
                .name(courseRequest.name())
                .language(courseRequest.language())
                .build();
    }

    public CourseResponse courseToCourseResponse(Course course) {
        return new CourseResponse(course.getId(), course.getName());
    }
}
