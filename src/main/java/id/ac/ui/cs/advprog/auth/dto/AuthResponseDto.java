package id.ac.ui.cs.advprog.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String message;
    private boolean success;
    private TokenResponse token;

}