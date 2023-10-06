package pl.adrianpacholak.attendanceservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "lesson-service", path = "/lessons")
public interface LessonClient {

    @GetMapping("/exists/{lessonId}")
    Map<String, Boolean> checkLessonExists(@PathVariable Integer lessonId);
}
