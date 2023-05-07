package id.ac.ui.cs.advprog.auth.dto.dataharian;

import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarian;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarianDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataHarianAdminResponse {
    private Integer userId;
    private Integer dataHarianId;
    private Date tanggal;
    private List<DataHarianDetailsData> dataHarianDetailsData;

    public static DataHarianAdminResponse fromDataHarian(DataHarian dataHarian, List<DataHarianDetails> dataHarianDetails) {
        return DataHarianAdminResponse.builder()
                .userId(dataHarian.getUser().getId())
                .dataHarianId(dataHarian.getId())
                .tanggal(dataHarian.getTanggal())
                .dataHarianDetailsData(dataHarianDetails
                        .stream()
                        .map(DataHarianDetailsData::fromDataHarianDetails)
                        .collect(Collectors.toList()))
                .build();
    }
}
