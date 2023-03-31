package id.ac.ui.cs.advprog.auth.model.dataharian;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import id.ac.ui.cs.advprog.auth.model.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_medicine")
public class DataHarian {
    @Id
    @GeneratedValue
    private Integer id;
    private Date orderDate;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "_user_id", nullable = false)
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<DataHarianDetails> orderDetailsList;
}
