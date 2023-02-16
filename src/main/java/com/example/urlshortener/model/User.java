package com.example.urlshortener.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "USERS", indexes = @Index(columnList = "email"))
@NoArgsConstructor @Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Email(message = "{errors.invalidEmail}")
    String email;

    public User(String email) {
        this.email = email;
    }
}
