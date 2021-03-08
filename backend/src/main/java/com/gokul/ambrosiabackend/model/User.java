package com.gokul.ambrosiabackend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {
    @Id
    @SequenceGenerator(name="USER_GEN",sequenceName = "SEQ_USER",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "USER_GEN")
    private Long userId;
    @NotBlank(message = "Username is Required")
    private String username;
    @NotBlank(message = "Password is Required")
    private String password;
    @Email
    @NotBlank(message = "Email is required")
    private String email;
    private Instant creationDate;
    private boolean accountStatus;

}
