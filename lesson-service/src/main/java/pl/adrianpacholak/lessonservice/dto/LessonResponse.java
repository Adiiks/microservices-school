package pl.adrianpacholak.lessonservice.dto;

import pl.adrianpacholak.lessonservice.model.EType;

public record LessonResponse(
        Integer id,
        EType type,
        TeacherResponse teacher,
        CourseResponse course,
        Integer totalStudentsSigned,
        Integer limitOfPlaces
) {
}
