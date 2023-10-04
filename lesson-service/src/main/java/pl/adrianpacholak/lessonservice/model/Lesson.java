package pl.adrianpacholak.lessonservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer courseId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ETerm term;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @Column(nullable = false)
    private String beginTime;

    @Column(nullable = false)
    private String endTime;

    @Column(nullable = false)
    private Integer classroom;

    @Column(nullable = false)
    private Integer totalStudentsSigned = 0;

    @Column(nullable = false)
    private Integer limitOfPlaces;

    @Column(nullable = false)
    private Integer teacherId;

    @ElementCollection
    private Set<String> students = new HashSet<>();

    public boolean isStudentSignUp(String username) {
        return students.contains(username);
    }
}
