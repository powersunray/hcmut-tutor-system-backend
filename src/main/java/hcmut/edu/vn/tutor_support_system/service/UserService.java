package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.CreateUserRequest;
import hcmut.edu.vn.tutor_support_system.dto.UpdateUserRequest;
import hcmut.edu.vn.tutor_support_system.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
  UserDto createUser(CreateUserRequest request);

  UserDto getUser(UUID id);

  Page<UserDto> listUsers(Pageable pageable);

  UserDto updateUser(UUID id, UpdateUserRequest request);

  void deleteUser(UUID id);
}
