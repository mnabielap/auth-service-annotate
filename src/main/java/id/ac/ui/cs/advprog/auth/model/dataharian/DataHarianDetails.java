package id.ac.ui.cs.advprog.auth.model.dataharian;

import id.ac.ui.cs.advprog.auth.model.makanan.Makanan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "data_harian_details_dummy")
public class DataHarianDetails implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private DataHarian dataHarian;
    @ManyToOne
    private Makanan makanan;
    private Integer quantity;
    private Integer totalKalori;
    @Serial
    private static final long serialVersionUID = 3L;
}
