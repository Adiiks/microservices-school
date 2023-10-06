package pl.adrianpacholak.attendanceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.attendanceservice.model.AttendanceList;

public interface AttendanceListRepository extends JpaRepository<AttendanceList, Integer> {
}
