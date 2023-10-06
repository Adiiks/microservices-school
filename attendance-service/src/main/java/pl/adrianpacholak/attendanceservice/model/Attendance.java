package pl.adrianpacholak.attendanceservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attendancies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer studentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EStatus status;

    private String comment;
}
