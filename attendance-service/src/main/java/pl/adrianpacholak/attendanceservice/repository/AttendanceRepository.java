package pl.adrianpacholak.attendanceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adrianpacholak.attendanceservice.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
}
