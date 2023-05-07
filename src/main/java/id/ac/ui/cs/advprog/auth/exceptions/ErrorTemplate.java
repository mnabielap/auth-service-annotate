package id.ac.ui.cs.advprog.auth.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorTemplate {
    String message;
    HttpStatus httpStatus;
    ZonedDateTime timestamp;
}
