package pl.adrianpacholak.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import pl.adrianpacholak.userservice.dto.FacultyDTO;
import pl.adrianpacholak.userservice.dto.UserResponse;
import pl.adrianpacholak.userservice.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Search users by full name")
    @Test
    void searchUsersByFullName() throws Exception {
        FacultyDTO facultyDTO = new FacultyDTO(1, "Faculty of Computer Science");
        UserResponse userResponse = new UserResponse(1, "Jan", "Kowalski", "jan@gmail.com", "teacher", "www.home.pl",
                facultyDTO);
        Page<UserResponse> users = new PageImpl<>(List.of(userResponse), Pageable.ofSize(20), 1);

        when(userService.searchUsersByFullName(anyString(), any()))
                .thenReturn(users);

        mockMvc.perform(get("/users/search/fullName")
                        .param("fullName", "Jan Kowalski")
                        .param("size", "20")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }
}