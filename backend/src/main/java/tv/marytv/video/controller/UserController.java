package tv.marytv.video.controller;

import tv.marytv.video.dto.ApiResponse;
import tv.marytv.video.dto.UserDto;
import tv.marytv.video.dto.UserUpsertDto;
import tv.marytv.video.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Users retrieved", users));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserUpsertDto dto) {
        UserDto user = userService.createUser(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, 201, "User created", user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id, @RequestBody UserUpsertDto dto) {
        UserDto updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "User updated", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "User deleted", null));
    }
}