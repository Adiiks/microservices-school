package pl.adrianpacholak.lessonservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pl.adrianpacholak.lessonservice.dto.TeacherResponse;

import java.util.List;
import java.util.Map;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/teachers/exists/{teacherId}")
    Map<String, Boolean> checkTeacherExists(@PathVariable Integer teacherId);

    @PostMapping("/teachers/ids")
    List<TeacherResponse> getTeachersByIds(@RequestBody List<Integer> ids);

    @GetMapping("/students/exists/username")
    Map<String, Boolean> checkStudentExists(@RequestParam String username);
}
