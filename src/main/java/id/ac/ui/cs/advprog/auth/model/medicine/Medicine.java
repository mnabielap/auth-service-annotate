package id.ac.ui.cs.advprog.auth.model.medicine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import id.ac.ui.cs.advprog.auth.model.order.OrderDetails;
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
public class Medicine {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    private MedicineCategory category;
    private String dose;
    private Integer stock;
    private Integer price;
    private String manufacturer;
    @JsonIgnore
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetailsList;
}
