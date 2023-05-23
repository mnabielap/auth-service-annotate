package id.ac.ui.cs.advprog.auth.model.makanan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarianDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "makanan_dummy")
public class Makanan implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    private MakananCategory category;
    private String keterangan;
    private Integer kalori;
    private String manufacturer;
    @Serial
    private static final long serialVersionUID = 4L;
    @JsonIgnore
    @OneToMany(mappedBy = "makanan", cascade = CascadeType.ALL)
    private List<DataHarianDetails> dataHarianDetailsList;
}
