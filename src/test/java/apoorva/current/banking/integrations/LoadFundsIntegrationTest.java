package apoorva.current.banking.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import apoorva.current.banking.dto.Amount;
import apoorva.current.banking.dto.LoadRequest;
import apoorva.current.banking.dto.LoadResponse;
import apoorva.current.banking.entity.Transaction;

@ComponentScan
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig
public class LoadFundsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testLoadFunds() {
        // Setup
        LoadRequest request = new LoadRequest();
        request.setUserId("user12");
        request.setMessageId("msg001");
        Amount amount = new Amount(new BigDecimal("50.00"), "USD", Transaction.DebitCredit.CREDIT);
        request.setTransactionAmount(amount);

        HttpEntity<LoadRequest> requestEntity = new HttpEntity<>(request);

        // Execute using PUT
        ResponseEntity<LoadResponse> responseEntity = restTemplate.exchange(
            "/load", // Endpoint URL
            HttpMethod.PUT, // Use PUT method
            requestEntity, // HTTP Entity containing request body
            LoadResponse.class // Response type
        );


        // Verify
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        LoadResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(new BigDecimal("50.00"), response.getBalance().getAmount());
    }


}
