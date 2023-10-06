package pl.adrianpacholak.attendanceservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.adrianpacholak.attendanceservice.dto.AttendanceResponse;
import pl.adrianpacholak.attendanceservice.service.AttendanceService;

@RestController
@RequestMapping("/attendancies")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/{attendanceId}")
    public AttendanceResponse getAttendance(@PathVariable Integer attendanceId) {
        return attendanceService.getAttendance(attendanceId);
    }
}
