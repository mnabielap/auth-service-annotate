package id.ac.ui.cs.advprog.auth.dto.dataharian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataHarianRequest {
    private List<DataHarianDetailsData> orderDetailsData;
}
