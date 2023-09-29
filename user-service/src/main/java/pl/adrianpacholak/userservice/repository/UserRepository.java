package pl.adrianpacholak.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.adrianpacholak.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT user FROM User user " +
            "WHERE UPPER(CONCAT(user.names, ' ', user.lastname)) LIKE UPPER(CONCAT('%', :fullName, '%'))")
    Page<User> findByFullNameLike(String fullName, Pageable pageable);

    Page<User> findByFacultyId(Integer facultyId, Pageable pageable);
}
