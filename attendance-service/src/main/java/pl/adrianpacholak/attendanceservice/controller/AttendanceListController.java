package pl.adrianpacholak.attendanceservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.adrianpacholak.attendanceservice.dto.AttendanceListRequest;
import pl.adrianpacholak.attendanceservice.service.AttendanceListService;

@RestController
@RequestMapping("/attendance-lists")
@RequiredArgsConstructor
public class AttendanceListController {

    private final AttendanceListService attendanceListService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAttendanceList(@Valid @RequestBody AttendanceListRequest request) {
        attendanceListService.createAttendanceList(request);
    }
}
