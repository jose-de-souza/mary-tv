package tv.marytv.video.service;

import tv.marytv.video.dto.UserDto;
import tv.marytv.video.dto.UserUpsertDto;
import tv.marytv.video.entity.User;
import tv.marytv.video.mapper.UserMapper;
import tv.marytv.video.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(UserUpsertDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = userMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public UserDto updateUser(Long id, UserUpsertDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateEntityFromDto(dto, user);
        if (dto.password() != null) {
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
        }
        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}