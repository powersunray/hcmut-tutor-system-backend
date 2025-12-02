package hcmut.edu.vn.tutor_support_system.service.impl;

import hcmut.edu.vn.tutor_support_system.dto.CreateUserRequest;
import hcmut.edu.vn.tutor_support_system.dto.UpdateUserRequest;
import hcmut.edu.vn.tutor_support_system.dto.UserDto;
import hcmut.edu.vn.tutor_support_system.entity.User;
import hcmut.edu.vn.tutor_support_system.exception.DuplicateResourceException;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.UserRepository;
import hcmut.edu.vn.tutor_support_system.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDto createUser(CreateUserRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateResourceException("User with email already exists: " + request.getEmail());
    }
    User toSave = DtoMapper.toUserEntity(request);
    User saved = userRepository.save(toSave);
    return DtoMapper.toUserDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto getUser(UUID id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    return DtoMapper.toUserDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserDto> listUsers(Pageable pageable) {
    return userRepository.findAll(pageable).map(DtoMapper::toUserDto);
  }

  @Override
  @Transactional
  public UserDto updateUser(UUID id, UpdateUserRequest request) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

    if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
      if (userRepository.existsByEmail(request.getEmail())) {
        throw new DuplicateResourceException("Email already in use: " + request.getEmail());
      }
      user.setEmail(request.getEmail());
    }
    if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
    if (request.getLastName() != null) user.setLastName(request.getLastName());
    if (request.getRole() != null) user.setRole(request.getRole());

    User updated = userRepository.save(user);
    return DtoMapper.toUserDto(updated);
  }

  @Override
  @Transactional
  public void deleteUser(UUID id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    userRepository.delete(user);
  }
}
