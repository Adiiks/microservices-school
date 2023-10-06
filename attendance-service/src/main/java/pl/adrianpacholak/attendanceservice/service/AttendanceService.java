package pl.adrianpacholak.attendanceservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.attendanceservice.client.UserClient;
import pl.adrianpacholak.attendanceservice.converter.AttendanceConverter;
import pl.adrianpacholak.attendanceservice.dto.AttendanceResponse;
import pl.adrianpacholak.attendanceservice.dto.StudentResponse;
import pl.adrianpacholak.attendanceservice.model.Attendance;
import pl.adrianpacholak.attendanceservice.repository.AttendanceRepository;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserClient userClient;
    private final AttendanceConverter attendanceConverter;

    public AttendanceResponse getAttendance(Integer attendanceId) {
        Attendance attendance = findAttendance(attendanceId);
        StudentResponse student = getStudent(attendance.getStudentId());

        return attendanceConverter.attendanceToAttendanceResponse(attendance, student);
    }

    private Attendance findAttendance(Integer attendanceId) {
        return attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Attendance with ID: %d not found.", attendanceId)));
    }

    private StudentResponse getStudent(Integer studentId) {
        return userClient.getStudent(studentId);
    }
}
