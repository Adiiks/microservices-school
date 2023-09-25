package pl.adrianpacholak.facultyservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.adrianpacholak.facultyservice.dto.FacultyRequest;
import pl.adrianpacholak.facultyservice.service.FacultyService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Create new faculty - Validation failed")
    @Test
    void createFacultyBodyInvalid() throws Exception {
        FacultyRequest request = new FacultyRequest("", "", "1234sfsfsdfs");

        mockMvc.perform(post("/faculties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @DisplayName("Create new faculty - SUCCESS")
    @Test
    void createFaculty() throws Exception {
        FacultyRequest request = new FacultyRequest("Computer Science", "ul.Mikolajki 21, Poznań", "123456789");

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @DisplayName("Check if faculty with particular id exists")
    @Test
    void checkFacultyExists() throws Exception {
        when(facultyService.checkFacultyExists(anyInt()))
                .thenReturn(true);

        mockMvc.perform(get("/faculties/exists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("exists", is(true)));
    }
}