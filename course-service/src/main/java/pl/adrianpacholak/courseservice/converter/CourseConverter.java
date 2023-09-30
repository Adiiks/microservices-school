package pl.adrianpacholak.courseservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.courseservice.dto.CourseRequest;
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
}
