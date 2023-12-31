package pl.adrianpacholak.lessonservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.adrianpacholak.lessonservice.dto.LessonRequest;
import pl.adrianpacholak.lessonservice.service.LessonService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createLesson(@Valid @RequestBody LessonRequest request) {
        lessonService.createLesson(request);
    }

    @GetMapping("/exists/{lessonId}")
    public Map<String, Boolean> checkLessonExists(@PathVariable Integer lessonId) {
        return Collections.singletonMap("exists", lessonService.checkLessonExists(lessonId));
    }
}
