package id.ac.ui.cs.advprog.auth.service.dataharian;

import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianRequest;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianAdminResponse;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianUserResponse;
import id.ac.ui.cs.advprog.auth.model.order.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataHarianService {
    List<DataHarianAdminResponse> findAll();
    List<DataHarianUserResponse> findAllByUserId(Integer userId);
    Order create(Integer userId, DataHarianRequest orderRequest);
    Order update(Integer userId, Integer id, DataHarianRequest orderRequest);
    void delete(Integer id);
}
