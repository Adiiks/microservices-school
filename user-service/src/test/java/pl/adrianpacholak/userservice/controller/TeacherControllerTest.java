package pl.adrianpacholak.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.adrianpacholak.userservice.dto.TeacherDTO;
import pl.adrianpacholak.userservice.dto.UserDTO;
import pl.adrianpacholak.userservice.service.TeacherService;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create new teacher - Validation failed")
    void createTeacherValidationFail() throws Exception {
        UserDTO userDTO = new UserDTO("123aaa1312", "", "", "sssssss.com", "", "");
        TeacherDTO teacherDTO = new TeacherDTO(userDTO, "33333", -10);

        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(8)));
    }

    @Test
    @DisplayName("Create new teacher - Success")
    void createTeacher() throws Exception {
        UserDTO userDTO = new UserDTO("57101805724", "Jan", "Kowalski", "jan@gmail.com", "password", "lecturer");
        TeacherDTO teacherDTO = new TeacherDTO(userDTO, "123456789", 10);

        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO)))
                .andExpect(status().isCreated());
    }
}