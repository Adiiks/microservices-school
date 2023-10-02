package pl.adrianpacholak.lessonservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import pl.adrianpacholak.lessonservice.model.EStatus;
import pl.adrianpacholak.lessonservice.model.ETerm;
import pl.adrianpacholak.lessonservice.model.EType;

import java.time.DayOfWeek;

public record LessonRequest(
        @NotNull(message = "Course ID is required")
        Integer courseId,
        @NotNull(message = "Term is required: WINTER or SUMMER")
        ETerm term,
        @NotNull(message = "Status is required: ENDED, ONGOING, UPCOMING")
        EStatus status,
        @NotNull(message = "Type is required: LECTURE, PRACTICAL, LABORATORY OR SEMINAR")
        EType type,
        @NotNull(message = "Day of the week is required: example. MONDAY, ...")
        DayOfWeek day,
        @Pattern(regexp = "([0-1][0-9]|2[0-3]):[0-5][0-9]", message = "Valid format is: HH:MM")
        String beginTime,
        @Pattern(regexp = "([0-1][0-9]|2[0-3]):[0-5][0-9]", message = "Valid format is: HH:MM")
        String endTime,
        @Positive(message = "Number of classroom must be a positive number")
        @NotNull(message = "Number of classroom is required")
        Integer classroom,
        @Positive(message = "Limit of places must be a positive number")
        @NotNull(message = "Limit of places is required")
        Integer limitOfPlaces,
        @NotNull(message = "Teacher ID is required")
        Integer teacherId
) {
}
