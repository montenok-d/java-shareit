package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "users")
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(unique = true)
    private String email;
}
