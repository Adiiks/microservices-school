package pl.adrianpacholak.attendanceservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.attendanceservice.dto.AttendanceRequest;
import pl.adrianpacholak.attendanceservice.dto.AttendanceResponse;
import pl.adrianpacholak.attendanceservice.dto.StudentResponse;
import pl.adrianpacholak.attendanceservice.model.Attendance;

@Component
public class AttendanceConverter {
    public Attendance attendaceRequestToAttendance(AttendanceRequest request) {
        return Attendance.builder()
                .studentId(request.studentId())
                .status(request.status())
                .comment(request.comment())
                .build();
    }

    public AttendanceResponse attendanceToAttendanceResponse(Attendance attendance, StudentResponse student) {
        return new AttendanceResponse(attendance.getId(), student, attendance.getStatus(), attendance.getComment());
    }
}
