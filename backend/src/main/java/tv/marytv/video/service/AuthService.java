package tv.marytv.video.service;

import tv.marytv.video.dto.LoginRequest;
import tv.marytv.video.dto.LoginResponse;
import tv.marytv.video.dto.UserUpsertDto;
import tv.marytv.video.entity.User;
import tv.marytv.video.repository.UserRepository;
import tv.marytv.video.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        String token = jwtUtil.generateToken(authentication);
        return new LoginResponse(token);
    }

    public void register(UserUpsertDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setUsername(dto.username());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        // Set ADMIN for "admin" username
        user.setRole("admin".equals(dto.username()) ? "ADMIN" : (dto.role() != null ? dto.role() : "USER"));
        userRepository.save(user);
    }
}