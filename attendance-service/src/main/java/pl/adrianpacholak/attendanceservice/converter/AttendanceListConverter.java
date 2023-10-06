package pl.adrianpacholak.attendanceservice.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.adrianpacholak.attendanceservice.dto.AttendanceListRequest;
import pl.adrianpacholak.attendanceservice.model.Attendance;
import pl.adrianpacholak.attendanceservice.model.AttendanceList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AttendanceListConverter {

    private final AttendanceConverter attendanceConverter;

    public AttendanceList attendanceListRequestToAttendanceList(AttendanceListRequest request) {
        List<Attendance> attendances = request.attendancies()
                .stream()
                .map(attendanceConverter::attendaceRequestToAttendance)
                .toList();

        return AttendanceList.builder()
                .lessonId(request.lessonId())
                .date(LocalDate.parse(request.date()))
                .attendances(attendances)
                .build();
    }
}
