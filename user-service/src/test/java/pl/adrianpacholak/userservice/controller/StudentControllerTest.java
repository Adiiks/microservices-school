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
import pl.adrianpacholak.userservice.dto.StudentDTO;
import pl.adrianpacholak.userservice.dto.UserDTO;
import pl.adrianpacholak.userservice.service.StudentService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @InjectMocks
    private StudentController studentController;

    @Mock
    private StudentService studentService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Create new student - Validation failed")
    void createStudentValidationFail() throws Exception {
        UserDTO userDTO = new UserDTO("123aaa1312", "", "", "sssssss.com", "", "", null);
        StudentDTO studentDTO = new StudentDTO(userDTO);

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(7)));
    }

    @Test
    @DisplayName("Create new student - Success")
    void createStudent() throws Exception {
        UserDTO userDTO = new UserDTO("57101805724", "Jan", "Kowalski", "jan@gmail.com", "password", "lecturer", 12);
        StudentDTO studentDTO = new StudentDTO(userDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isCreated());
    }

    @DisplayName("Check if student exists based on username")
    @Test
    void checkStudentExists() throws Exception {
        when(studentService.checkStudentExists(anyString()))
                .thenReturn(true);

        mockMvc.perform(get("/students/exists/username")
                .param("username", "username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("exists", is(true)));
    }

    @DisplayName("Get student by ID")
    @Test
    void getStudent() throws Exception {
        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk());
    }

    @DisplayName("Get students by list of IDs")
    @Test
    void getStudentsByIds() throws Exception {
        mockMvc.perform(post("/students/ids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(1, 2))))
                .andExpect(status().isOk());
    }
}