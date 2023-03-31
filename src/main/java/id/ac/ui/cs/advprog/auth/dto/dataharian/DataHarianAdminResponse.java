package id.ac.ui.cs.advprog.auth.dto.dataharian;

import id.ac.ui.cs.advprog.auth.model.order.Order;
import id.ac.ui.cs.advprog.auth.model.order.OrderDetails;
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

    public static DataHarianAdminResponse fromOrder(Order order, List<OrderDetails> orderDetails) {
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
