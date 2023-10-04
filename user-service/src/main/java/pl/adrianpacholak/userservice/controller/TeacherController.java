package pl.adrianpacholak.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.adrianpacholak.userservice.dto.TeacherDTO;
import pl.adrianpacholak.userservice.dto.TeacherResponse;
import pl.adrianpacholak.userservice.service.TeacherService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        teacherService.createTeacher(teacherDTO);
    }

    @GetMapping("/exists/{teacherId}")
    public Map<String, Boolean> checkTeacherExists(@PathVariable Integer teacherId) {
        return Collections.singletonMap("exists", teacherService.checkTeacherExists(teacherId));
    }

    @PostMapping("/ids")
    public List<TeacherResponse> getTeachersByIds(@RequestBody List<Integer> ids) {
        return teacherService.getTeachersByIds(ids);
    }
}
