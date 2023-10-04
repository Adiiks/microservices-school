package pl.adrianpacholak.userservice.controller;

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
import pl.adrianpacholak.userservice.dto.TeacherDTO;
import pl.adrianpacholak.userservice.dto.UserDTO;
import pl.adrianpacholak.userservice.service.TeacherService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Create new teacher - Validation failed")
    void createTeacherValidationFail() throws Exception {
        UserDTO userDTO = new UserDTO("123aaa1312", "", "", "sssssss.com", "", "", null);
        TeacherDTO teacherDTO = new TeacherDTO(userDTO, "33333", -10);

        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(9)));
    }

    @Test
    @DisplayName("Create new teacher - Success")
    void createTeacher() throws Exception {
        UserDTO userDTO = new UserDTO("57101805724", "Jan", "Kowalski", "jan@gmail.com", "password", "lecturer", 12);
        TeacherDTO teacherDTO = new TeacherDTO(userDTO, "123456789", 10);

        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO)))
                .andExpect(status().isCreated());
    }

    @DisplayName("Check if teacher exists based on ID")
    @Test
    void checkTeacherExists() throws Exception {
        when(teacherService.checkTeacherExists(anyInt())).thenReturn(true);

        mockMvc.perform(get("/teachers/exists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("exists", is(true)));
    }

    @DisplayName("Get list of teachers based on list of IDs")
    @Test
    void getTeachersByIds() throws Exception {
        mockMvc.perform(get("/teachers/ids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(1, 2, 3))))
                .andExpect(status().isOk());
    }
}