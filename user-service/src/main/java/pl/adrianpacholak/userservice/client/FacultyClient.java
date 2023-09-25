package pl.adrianpacholak.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "faculty-service", path = "/faculties")
public interface FacultyClient {

    @GetMapping("/exists/{facultyId}")
    Map<String, Boolean> checkFacultyExists(@PathVariable Integer facultyId);
}
