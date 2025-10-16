package tv.marytv.video.service;

import tv.marytv.video.dto.UserDto;
import tv.marytv.video.dto.UserUpsertDto;
import tv.marytv.video.entity.User;
import tv.marytv.video.mapper.UserMapper;
import tv.marytv.video.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Transactional
    public UserDto createUser(UserUpsertDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPasswordHash(passwordEncoder.encode(userDto.password()));
        user.setRole(userDto.role() != null ? userDto.role() : "ADMIN");
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public Optional<UserDto> updateUser(Long id, UserUpsertDto userDto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(userDto.username());

                    if (userDto.password() != null && !userDto.password().isEmpty()) {
                        existingUser.setPasswordHash(passwordEncoder.encode(userDto.password()));
                    }

                    if (userDto.role() != null) {
                        existingUser.setRole(userDto.role());
                    }

                    User updatedUser = userRepository.save(existingUser);
                    return userMapper.toDto(updatedUser);
                });
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}