package pl.adrianpacholak.attendanceservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.attendanceservice.dto.AttendanceRequest;
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
}
