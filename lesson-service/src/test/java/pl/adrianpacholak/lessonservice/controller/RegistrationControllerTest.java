package pl.adrianpacholak.lessonservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.adrianpacholak.lessonservice.dto.RegistrationRequest;
import pl.adrianpacholak.lessonservice.dto.RegistrationResponse;
import pl.adrianpacholak.lessonservice.service.RegistrationService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private RegistrationService registrationService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @DisplayName("Create new registration - body invalid")
    @Test
    void openRegistration_BodyInvalid() throws Exception {
        RegistrationRequest request = new RegistrationRequest("12-00-45", "45-13:32", "12-00-45", "45-13:32", null);

        mockMvc.perform(post("/lessons/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(5)));
    }

    @DisplayName("Create new registration")
    @Test
    void openRegistration() throws Exception {
        RegistrationRequest request = new RegistrationRequest("2023-10-03", "08:00:00", "2023-10-13", "23:59:59", 1);

        mockMvc.perform(post("/lessons/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @DisplayName("Get list of registrations")
    @Test
    void getRegistrations() throws Exception {
        Page<RegistrationResponse> response = new PageImpl<>(List.of(), Pageable.unpaged(), 0);

        when(registrationService.getRegistrations(anyString(), any())).thenReturn(response);

        mockMvc.perform(get("/lessons/registrations")
                .param("page", "0")
                .param("size", "10")
                .header("username", "12345678901"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}