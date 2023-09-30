package pl.adrianpacholak.facultyservice.controller;

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
import pl.adrianpacholak.facultyservice.dto.FacultyRequest;
import pl.adrianpacholak.facultyservice.dto.FacultyResponse;
import pl.adrianpacholak.facultyservice.service.FacultyService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FacultyControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private FacultyController facultyController;

    @Mock
    private FacultyService facultyService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(facultyController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

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

    @DisplayName("Get faculties by list of ids")
    @Test
    void getFacultiesByIds() throws Exception {
        List<Integer> facultiesIds = List.of(1);
        Map<Integer, String> faculties = Collections.singletonMap(1, "Faculty of Computer Science");

        when(facultyService.getFacultiesByIds(facultiesIds))
                .thenReturn(faculties);

        mockMvc.perform(post("/faculties/search/ids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facultiesIds)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(faculties)));
    }

    @DisplayName("Get faculty ID based on faculty name")
    @Test
    void getFacultyId() throws Exception {
        Map<String, Integer> response = Collections.singletonMap("facultyId", 1);

        when(facultyService.getFacultyIdByName(anyString())).thenReturn(response);

        mockMvc.perform(get("/faculties/faculty-name/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("facultyId", is(1)));
    }

    @DisplayName("Get list of all faculties")
    @Test
    void getFaculties() throws Exception {
        Page<FacultyResponse> faculties = new PageImpl<>(List.of(buildFacultyResponse()), Pageable.unpaged(), 1);

        when(facultyService.getFaculties(any())).thenReturn(faculties);

        mockMvc.perform(get("/faculties")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(faculties)));
    }

    private FacultyResponse buildFacultyResponse() {
        return new FacultyResponse(1, "Faculty of Computer Science", "ul.Mikołajki 43", "123456789");
    }
}