package id.ac.ui.cs.advprog.auth.dto.dataharian;

import id.ac.ui.cs.advprog.auth.model.auth.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataHarianSummaryResponse {
    private String tanggal;
    private Integer targetKaloriPerHari;
    private Integer totalKaloriPerHari;
    private List<DataHarianUserResponse> dataHarianUserResponseList;

    public static DataHarianSummaryResponse createSummaryResponse(User user, List<DataHarianUserResponse> response) {
        Integer totalKaloriSum = 0;
        for (DataHarianUserResponse userResponse: response) {
            for (DataHarianDetailsData data: userResponse.getDataHarianDetailsData()) {
                totalKaloriSum += data.getTotalKalori();
            }
        }

        List<DataHarianUserResponse> onlyTodayData = new ArrayList<>();
        for (DataHarianUserResponse dataHarianUserResponse: response) {
            if (isToday(dataHarianUserResponse.getTanggal())) {
                onlyTodayData.add(dataHarianUserResponse);
            }
        }

        return DataHarianSummaryResponse.builder()
                .tanggal(java.time.LocalDate.now()+"")
                .targetKaloriPerHari(user.getTargetKalori())
                .totalKaloriPerHari(totalKaloriSum)
                .dataHarianUserResponseList(onlyTodayData)
                .build();
    }

    private static boolean isToday(Date date){
        var today = Calendar.getInstance();
        var specifiedDate  = Calendar.getInstance();
        specifiedDate.setTime(date);

        return today.get(Calendar.DAY_OF_MONTH) == specifiedDate.get(Calendar.DAY_OF_MONTH)
                &&  today.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH)
                &&  today.get(Calendar.YEAR) == specifiedDate.get(Calendar.YEAR);
    }

}