package id.ac.ui.cs.advprog.auth.model.dataharian;

import id.ac.ui.cs.advprog.auth.model.makanan.Makanan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DataHarianDetails {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private DataHarian dataHarian;
    @ManyToOne
    private Makanan makanan;
    private Integer quantity;
    private Integer totalKalori;
}
