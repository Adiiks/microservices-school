package pl.adrianpacholak.courseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.adrianpacholak.courseservice.dto.CourseRequest;
import pl.adrianpacholak.courseservice.dto.CourseResponse;
import pl.adrianpacholak.courseservice.service.CourseService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourse(@Valid @RequestBody CourseRequest request) {
        courseService.createCourse(request);
    }

    @GetMapping("/exists/{courseId}")
    public Map<String, Boolean> checkCourseExists(@PathVariable Integer courseId) {
        return Collections.singletonMap("exists", courseService.checkCourseExists(courseId));
    }

    @GetMapping("/ids")
    public List<CourseResponse> getCoursesByIds(@RequestBody List<Integer> ids) {
        return courseService.getCoursesByIds(ids);
    }
}
