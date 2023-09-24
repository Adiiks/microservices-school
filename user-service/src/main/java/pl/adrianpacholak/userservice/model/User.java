package pl.adrianpacholak.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @SuperBuilder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer pesel;

    @Column(nullable = false)
    private String names;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String email;

    private String pageUrl;
}
