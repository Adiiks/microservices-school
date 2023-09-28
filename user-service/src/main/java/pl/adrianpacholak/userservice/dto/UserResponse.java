package pl.adrianpacholak.userservice.dto;

public record UserResponse(
        Integer id,
        String names,
        String lastname,
        String email,
        String position,
        String pageUrl,
        FacultyDTO faculty
) {
}
