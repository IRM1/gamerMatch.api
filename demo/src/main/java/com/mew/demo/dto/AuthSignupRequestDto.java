package com.mew.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

@Data
public class AuthSignupRequestDto {

  @NotBlank(message = "First name is required")
  private String firstName;

  private String lastName;

  @Past(message = "Date of birth must be in the past")
  @NotNull(message = "Date of birth is required")
  private LocalDate dob;

  @Email(message = "Invalid email format")
  @NotBlank(message = "WSU email is required")
  private String email;

  @NotBlank(message = "Gamertag is required")
  private String gamertag;

  @NotNull(message = "Preferred console must be specified")
  private Integer consoleId;

  private String aboutUser;

  private Set<Integer> gameIds;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  private String password;
}
