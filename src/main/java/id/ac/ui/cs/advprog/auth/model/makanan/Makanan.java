package id.ac.ui.cs.advprog.auth.model.makanan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarianDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Makanan {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    private MakananCategory category;
    private String dose;
    private Integer stock;
    private Integer price;
    private String manufacturer;
    @JsonIgnore
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL)
    private List<DataHarianDetails> orderDetailsList;
}
