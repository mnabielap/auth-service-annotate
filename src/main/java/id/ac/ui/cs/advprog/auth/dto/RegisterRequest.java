package id.ac.ui.cs.advprog.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    private String firstname;
    private String lastname;
    @NotNull
    private String username;
    @NotNull
    private String password;

    private String role;
    @NotNull
    private Integer targetKalori;
    @NotNull
    private String tanggalLahir;
    private Integer beratBadan;
    private Integer tinggiBadan;

}
