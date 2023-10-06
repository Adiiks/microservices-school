package pl.adrianpacholak.attendanceservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.attendanceservice.client.UserClient;
import pl.adrianpacholak.attendanceservice.converter.AttendanceConverter;
import pl.adrianpacholak.attendanceservice.dto.AttendanceResponse;
import pl.adrianpacholak.attendanceservice.dto.StudentResponse;
import pl.adrianpacholak.attendanceservice.model.Attendance;
import pl.adrianpacholak.attendanceservice.model.EStatus;
import pl.adrianpacholak.attendanceservice.repository.AttendanceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    private AttendanceService attendanceService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserClient userClient;

    private AttendanceConverter attendanceConverter = new AttendanceConverter();

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceService(attendanceRepository, userClient, attendanceConverter);
    }

    @DisplayName("Get attendance by id - not found")
    @Test
    void getAttendance_NotFound() {
        when(attendanceRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                attendanceService.getAttendance(1));
    }

    @DisplayName("Get attendance by id")
    @Test
    void getAttendance() {
        Attendance attendance = buildAttendance();
        StudentResponse student = buildStudent(attendance);

        when(attendanceRepository.findById(anyInt()))
                .thenReturn(Optional.of(attendance));

        when(userClient.getStudent(anyInt()))
                .thenReturn(student);

        AttendanceResponse response = attendanceService.getAttendance(attendance.getId());

        assertEquals(response.comment(), attendance.getComment());
        assertEquals(response.id(), attendance.getId());
        assertEquals(response.status(), attendance.getStatus());
        assertEquals(response.student().id(), student.id());
        assertEquals(response.student().fullName(), student.fullName());
    }

    private Attendance buildAttendance() {
        return Attendance.builder()
                .id(1)
                .comment("nothing special")
                .status(EStatus.PRESENT)
                .studentId(1)
                .build();
    }

    private StudentResponse buildStudent(Attendance attendance) {
        return new StudentResponse(attendance.getStudentId(), "Jan Kowalski");
    }
}