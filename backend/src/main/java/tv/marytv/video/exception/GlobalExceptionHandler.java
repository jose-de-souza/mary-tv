package tv.marytv.video.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tv.marytv.video.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, 400, e.getMessage(), null));
    }

    @ExceptionHandler(ItemHasChildrenException.class)
    public ResponseEntity<ApiResponse<Void>> handleItemHasChildren(ItemHasChildrenException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, 400, e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, 500, "An error occurred", null));
    }
}