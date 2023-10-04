package pl.adrianpacholak.lessonservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.adrianpacholak.lessonservice.dto.CourseResponse;

import java.util.List;
import java.util.Map;

@FeignClient(value = "course-service", path = "/courses")
public interface CourseClient {

    @GetMapping("/exists/{courseId}")
    Map<String, Boolean> checkCourseExists(@PathVariable Integer courseId);

    @PostMapping("/ids")
    List<CourseResponse> getCoursesByIds(@RequestBody List<Integer> ids);
}
