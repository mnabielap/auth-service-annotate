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
public class DataHarianUserResponse {
    private Integer dataHarianId;
    private Date tanggal;
    private List<DataHarianDetailsData> dataHarianDetailsData;

    public static DataHarianUserResponse fromDataHarian(DataHarian dataHarian, List<DataHarianDetails> dataHarianDetails) {
        List<DataHarianDetailsData> dataHarianDetailsDataList = dataHarianDetails
                .stream()
                .map(DataHarianDetailsData::fromDataHarianDetails)
                .collect(Collectors.toList());

        return DataHarianUserResponse.builder()
                .dataHarianId(dataHarian.getId())
                .tanggal(dataHarian.getTanggal())
                .dataHarianDetailsData(dataHarianDetailsDataList)
                .build();
    }
}