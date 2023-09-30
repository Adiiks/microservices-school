package pl.adrianpacholak.courseservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "faculty-service", path = "/faculties")
public interface FacultyClient {

    @GetMapping("/exists/{facultyId}")
    Map<String, Boolean> checkFacultyExists(@PathVariable Integer facultyId);
}
