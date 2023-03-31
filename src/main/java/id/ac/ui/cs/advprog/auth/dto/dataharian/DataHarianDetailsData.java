package id.ac.ui.cs.advprog.auth.dto.dataharian;

import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarianDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataHarianDetailsData {
    private Integer medicineId;
    private Integer quantity;
    private Integer totalPrice;

    public static DataHarianDetailsData fromOrderDetails(DataHarianDetails orderDetails) {
        return DataHarianDetailsData.builder()
                .medicineId(orderDetails.getMakanan().getId())
                .quantity(orderDetails.getQuantity())
                .totalPrice(orderDetails.getTotalPrice())
                .build();
    }
}
