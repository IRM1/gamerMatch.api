package com.mew.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendMessageRequestDto {

  @NotBlank(message = "Message body is required")
  @Size(max = 1000, message = "Messages must be 1000 characters or fewer")
  private String messageBody;
}
