package com.mew.demo.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthResponseDto {
  String sessionToken;
  UserDto user;
}
