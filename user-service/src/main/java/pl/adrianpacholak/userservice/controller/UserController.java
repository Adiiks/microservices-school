package pl.adrianpacholak.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.adrianpacholak.userservice.dto.UserResponse;
import pl.adrianpacholak.userservice.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/search/fullName")
    public Page<UserResponse> searchUsersByFullName(@RequestParam String fullName, Pageable pageable) {
        return userService.searchUsersByFullName(fullName, pageable);
    }

    @GetMapping("/search/faculty-name")
    public Page<UserResponse> searchUsersByFacultyName(@RequestParam String facultyName, Pageable pageable) {
        return userService.searchUsersByFacultyName(facultyName, pageable);
    }
}
