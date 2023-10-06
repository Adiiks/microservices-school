package pl.adrianpacholak.attendanceservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.adrianpacholak.attendanceservice.service.AttendanceService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AttendanceController attendanceController;

    @Mock
    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @DisplayName("Get attendance by ID")
    @Test
    void getAttendance() throws Exception {
        mockMvc.perform(get("/attendancies/1"))
                .andExpect(status().isOk());
    }
}