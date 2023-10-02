package pl.adrianpacholak.lessonservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/teachers/exists/{teacherId}")
    Map<String, Boolean> checkTeacherExists(@PathVariable Integer teacherId);
}
