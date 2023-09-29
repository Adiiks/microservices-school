package pl.adrianpacholak.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "faculty-service", path = "/faculties")
public interface FacultyClient {

    @GetMapping("/exists/{facultyId}")
    Map<String, Boolean> checkFacultyExists(@PathVariable Integer facultyId);

    @PostMapping("/search/ids")
    Map<Integer, String> getFacultiesByIds(@RequestBody Set<Integer> facultiesIds);

    @GetMapping("/faculty-name/{facultyName}")
    Map<String, Integer> getFacultyId(@PathVariable String facultyName);
}
