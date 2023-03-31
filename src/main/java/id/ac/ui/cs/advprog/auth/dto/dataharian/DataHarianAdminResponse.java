package id.ac.ui.cs.advprog.auth.dto.dataharian;

import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarian;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarianDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataHarianAdminResponse {
    private Integer userId;
    private Integer orderId;
    private Date orderDate;
    private List<DataHarianDetailsData> orderDetailsData;

    public static DataHarianAdminResponse fromOrder(DataHarian order, List<DataHarianDetails> orderDetails) {
        return DataHarianAdminResponse.builder()
                .userId(order.getUser().getId())
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderDetailsData(orderDetails
                        .stream()
                        .map(DataHarianDetailsData::fromOrderDetails)
                        .toList())
                .build();
    }
}
