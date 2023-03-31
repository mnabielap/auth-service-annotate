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
    private Integer totalKalori;

    public static DataHarianDetailsData fromDataHarianDetails(DataHarianDetails dataHarianDetails) {
        return DataHarianDetailsData.builder()
                .makananId(dataHarianDetails.getMakanan().getId())
                .quantity(dataHarianDetails.getQuantity())
                .totalKalori(dataHarianDetails.getTotalKalori())
                .build();
    }
}
