package com.mew.demo.dto;

import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

@Data
public class ProfileUpdateRequestDto {
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private String gamertag;
  private Integer consoleId;
  private String aboutUser;
  private Set<Integer> gameIds;
}
