package pl.adrianpacholak.lessonservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "course-service", path = "/courses")
public interface CourseClient {

    @GetMapping("/exists/{courseId}")
    Map<String, Boolean> checkCourseExists(@PathVariable Integer courseId);
}
