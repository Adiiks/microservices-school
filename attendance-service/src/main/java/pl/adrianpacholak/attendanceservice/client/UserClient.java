package pl.adrianpacholak.attendanceservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.adrianpacholak.attendanceservice.dto.StudentResponse;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/students/{studentId}")
    StudentResponse getStudent(@PathVariable Integer studentId);
}
