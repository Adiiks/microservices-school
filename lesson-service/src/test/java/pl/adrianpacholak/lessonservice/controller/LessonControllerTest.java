package pl.adrianpacholak.lessonservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.adrianpacholak.lessonservice.dto.LessonRequest;
import pl.adrianpacholak.lessonservice.model.EStatus;
import pl.adrianpacholak.lessonservice.model.ETerm;
import pl.adrianpacholak.lessonservice.model.EType;
import pl.adrianpacholak.lessonservice.service.LessonService;

import java.time.DayOfWeek;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private LessonController lessonController;

    @Mock
    private LessonService lessonService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @DisplayName("Create new lesson - body validation failed")
    @Test
    void createLesson_ValidationFailed() throws Exception {
        LessonRequest request = new LessonRequest(null, null, null, null, null, "1:23", "2121", -12, -100, null);

        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(10)));
    }

    @DisplayName("Create new lesson")
    @Test
    void createLesson() throws Exception {
        LessonRequest request = new LessonRequest(1, ETerm.SUMMER, EStatus.ENDED, EType.LABORATORY, DayOfWeek.FRIDAY,
                "08:15", "09:45", 112, 30, 1);

        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}