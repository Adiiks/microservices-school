package pl.adrianpacholak.facultyservice.dto;

public record FacultyResponse(
        Integer id,
        String name,
        String address,
        String phoneNumber
) {
}
