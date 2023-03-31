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
    private Integer makananId;
    private Integer quantity;
    private Integer totalPrice;

    public static DataHarianDetailsData fromDataHarianDetails(DataHarianDetails orderDetails) {
        return DataHarianDetailsData.builder()
                .makananId(orderDetails.getMakanan().getId())
                .quantity(orderDetails.getQuantity())
                .totalPrice(orderDetails.getTotalKalori())
                .build();
    }
}
