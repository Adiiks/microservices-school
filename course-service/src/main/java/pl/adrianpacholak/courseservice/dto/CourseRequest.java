package pl.adrianpacholak.courseservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pl.adrianpacholak.courseservice.model.ELanguage;

import java.math.BigDecimal;

public record CourseRequest(
        @NotBlank
        String name,
        @NotNull
        Integer facultyId,
        @Positive
        @NotNull
        BigDecimal ectsPoints,
        @NotNull(message = "Possible values: 'PL' or 'EN'")
        ELanguage language
) {
}
