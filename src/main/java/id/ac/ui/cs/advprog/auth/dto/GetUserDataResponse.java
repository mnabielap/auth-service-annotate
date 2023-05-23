package id.ac.ui.cs.advprog.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDataResponse {
    private String username;
    private Integer id;
    private Integer targetKalori;
    private Date tanggalLahir;
    private Integer beratBadan;
    private Integer tinggiBadan;

}