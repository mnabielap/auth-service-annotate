package id.ac.ui.cs.advprog.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MakananRequest {
    private String name;
    private String category;
    private String keterangan;
    private Integer kalori;
    private String manufacturer;
}
