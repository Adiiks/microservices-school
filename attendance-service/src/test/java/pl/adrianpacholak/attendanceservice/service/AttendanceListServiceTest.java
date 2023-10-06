package pl.adrianpacholak.attendanceservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.attendanceservice.client.LessonClient;
import pl.adrianpacholak.attendanceservice.converter.AttendanceConverter;
import pl.adrianpacholak.attendanceservice.converter.AttendanceListConverter;
import pl.adrianpacholak.attendanceservice.dto.AttendanceListRequest;
import pl.adrianpacholak.attendanceservice.dto.AttendanceRequest;
import pl.adrianpacholak.attendanceservice.model.AttendanceList;
import pl.adrianpacholak.attendanceservice.model.EStatus;
import pl.adrianpacholak.attendanceservice.repository.AttendanceListRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceListServiceTest {

    private AttendanceListService attendanceListService;

    @Mock
    private AttendanceListRepository attendanceListRepository;

    @Mock
    private LessonClient lessonClient;

    private AttendanceListConverter attendanceListConverter = new AttendanceListConverter(new AttendanceConverter());

    @Captor
    private ArgumentCaptor<AttendanceList> attendanceListAc;

    @BeforeEach
    void setUp() {
        attendanceListService = new AttendanceListService(attendanceListRepository, lessonClient,
                attendanceListConverter);
    }

    @DisplayName("Create attendance list - lesson not found")
    @Test
    void createAttendanceList_LessonNotFound() {
        AttendanceListRequest request = buildAttendanceListRequest();

        when(lessonClient.checkLessonExists(anyInt()))
                .thenReturn(Collections.singletonMap("exists", false));

        assertThrows(ResponseStatusException.class, () ->
                attendanceListService.createAttendanceList(request));
    }

    @DisplayName("Create attendance list")
    @Test
    void createAttendanceList() {
        AttendanceListRequest request = buildAttendanceListRequest();

        when(lessonClient.checkLessonExists(anyInt()))
                .thenReturn(Collections.singletonMap("exists", true));

        attendanceListService.createAttendanceList(request);

        verify(attendanceListRepository).save(attendanceListAc.capture());
        AttendanceList attendanceList = attendanceListAc.getValue();

        assertNull(attendanceList.getId());
        assertEquals(request.lessonId(), attendanceList.getLessonId());
        assertEquals(LocalDate.parse(request.date()), attendanceList.getDate());
        assertEquals(request.attendancies().size(), attendanceList.getAttendances().size());
    }

    private AttendanceRequest buildAttendanceRequest() {
        return new AttendanceRequest(1, EStatus.ABSENCE, "");
    }

    private AttendanceListRequest buildAttendanceListRequest() {
        return new AttendanceListRequest(1, "2023-10-06", List.of(buildAttendanceRequest()));
    }
}