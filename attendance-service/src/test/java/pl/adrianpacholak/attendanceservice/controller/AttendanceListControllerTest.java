package pl.adrianpacholak.attendanceservice.controller;

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
import pl.adrianpacholak.attendanceservice.dto.AttendanceListRequest;
import pl.adrianpacholak.attendanceservice.dto.AttendanceRequest;
import pl.adrianpacholak.attendanceservice.model.EStatus;
import pl.adrianpacholak.attendanceservice.service.AttendanceListService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AttendanceListControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AttendanceListController attendanceListController;

    @Mock
    private AttendanceListService attendanceListService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceListController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @DisplayName("Create attendance list - validation failed")
    @Test
    void createAttendanceList_InvalidBody() throws Exception {
        AttendanceRequest attendance = new AttendanceRequest(null, null, "");
        AttendanceListRequest request = new AttendanceListRequest(null, "12-1111-21", List.of(attendance));

        mockMvc.perform(post("/attendance-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(4)));
    }

    @DisplayName("Create attendance list")
    @Test
    void createAttendanceList() throws Exception {
        AttendanceRequest attendance = new AttendanceRequest(1, EStatus.ABSENCE, "");
        AttendanceListRequest request = new AttendanceListRequest(1, "2023-10-06", List.of(attendance));

        mockMvc.perform(post("/attendance-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}