package pl.adrianpacholak.courseservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer facultyId;

    @Column(nullable = false)
    private BigDecimal ectsPoints;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ELanguage language;
}
