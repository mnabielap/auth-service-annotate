package id.ac.ui.cs.advprog.auth.service.eventListener;

import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianRequest;
import id.ac.ui.cs.advprog.auth.model.auth.User;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;

@Service
public class UserCreatedEventListener implements ApplicationListener<UserCreatedEvent> {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${endpoint.url}")
    private String url;

    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = event.getUser();
        String token = "Bearer " + event.getJwtToken();

        // Perform the desired actions when a user is created
        // Example: Create daily data for the user using DataHarianService
        DataHarianRequest dataHarianRequest = buildDataHarianRequest(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<DataHarianRequest> entity = new HttpEntity<>(dataHarianRequest,headers);
        try {
            Object obj = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        }
        catch (Exception e){
            System.out.println("error");
            System.out.println(e);
        }

    }

    private DataHarianRequest buildDataHarianRequest(User user) {
        // Implement the logic to build the DataHarianRequest object with the necessary data
        // Return the DataHarianRequest object
        DataHarianRequest dataHarianRequest = DataHarianRequest.builder()
                .targetKalori((double) user.getTargetKalori())
                .build();

        return dataHarianRequest;
    }
}
