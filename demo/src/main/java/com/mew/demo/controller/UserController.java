package com.mew.demo.controller;

import com.mew.demo.dto.UserDto;
import com.mew.demo.exception.EntityNotFoundException;
import com.mew.demo.model.User;
import com.mew.demo.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST})
public class UserController {

  //    private final UserDtoMapper userDtoMapper;
  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserDto>> getAllUsers() throws EntityNotFoundException {

    List<User> users = userService.getAllUsers();
    List<UserDto> dtos = users.stream().map(UserDto::fromEntity).toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/id/{userId}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId)
      throws EntityNotFoundException {

    User user = userService.getUserById(userId);
    return ResponseEntity.ok(UserDto.fromEntity(user));
  }

  @GetMapping("/name/{firstName}")
  public ResponseEntity<UserDto> getUserByFirstName(@PathVariable String firstName)
      throws EntityNotFoundException {

    User user = userService.getUserByFirstName(firstName);
    return ResponseEntity.ok(UserDto.fromEntity(user));
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
      throws EntityNotFoundException {

    UserDto createdUser = userService.createUser(userDto);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @PutMapping(path = "/id/{userId}")
  public ResponseEntity<UserDto> updateUser(
      @PathVariable Integer userId, @RequestBody UserDto requestBody)
      throws EntityNotFoundException, IllegalArgumentException {

    if (requestBody.getUserId() != null && !requestBody.getUserId().equals(userId)) {
      throw new IllegalArgumentException(
          "UserId in body cannot be changed or differ from path ID.");
    }

    UserDto updateRequestBody = new UserDto(
        userId,
        requestBody.getFirstName(),
        requestBody.getLastName(),
        requestBody.getDob(),
        requestBody.getEmail(),
        requestBody.getGamertag(),
        requestBody.getConsoleId(),
        requestBody.getAboutUser(),
        requestBody.getGameIds());

    UserDto updatedUser = userService.updateUser(updateRequestBody);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping(path = "/id/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Integer userId)
      throws EntityNotFoundException {

    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }
}
