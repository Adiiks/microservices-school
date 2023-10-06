package pl.adrianpacholak.attendanceservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.attendanceservice.client.LessonClient;
import pl.adrianpacholak.attendanceservice.converter.AttendanceListConverter;
import pl.adrianpacholak.attendanceservice.dto.AttendanceListRequest;
import pl.adrianpacholak.attendanceservice.model.AttendanceList;
import pl.adrianpacholak.attendanceservice.repository.AttendanceListRepository;

@Service
@RequiredArgsConstructor
public class AttendanceListService {

    private final AttendanceListRepository attendanceListRepository;
    private final LessonClient lessonClient;
    private final AttendanceListConverter attendanceListConverter;

    @Transactional
    public void createAttendanceList(AttendanceListRequest request) {
        checkLessonExists(request.lessonId());

        AttendanceList attendanceList = attendanceListConverter.attendanceListRequestToAttendanceList(request);

        attendanceListRepository.save(attendanceList);
    }

    private void checkLessonExists(Integer lessonId) {
        if (!lessonClient.checkLessonExists(lessonId).get("exists")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Lesson with ID: %s not exists.", lessonId));
        }
    }
}
