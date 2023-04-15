package id.ac.ui.cs.advprog.auth.service.dataharian;

import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianRequest;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianAdminResponse;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianUserResponse;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarian;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataHarianService {
    List<DataHarianAdminResponse> findAll();
    List<DataHarianUserResponse> findAllByUserId(Integer userId);
    DataHarian create(Integer userId, DataHarianRequest dataHarianRequest);
    DataHarian update(Integer userId, Integer id, DataHarianRequest dataHarianRequest);
    void delete(Integer id);
}
