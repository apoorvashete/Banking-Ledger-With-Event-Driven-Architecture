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
import apoorva.current.banking.dto.AuthorizationRequest;
import apoorva.current.banking.dto.AuthorizationResponse;
import apoorva.current.banking.entity.Transaction;


@ComponentScan
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig
public class AuthorizeIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAuthorizeTransaction() {
        // Setup
        AuthorizationRequest request = new AuthorizationRequest();
        request.setUserId("user12");
        request.setMessageId("msg002");
        Amount amount = new Amount(new BigDecimal("50.00"), "USD", Transaction.DebitCredit.DEBIT);
        request.setTransactionAmount(amount);

        // Execute
        HttpEntity<AuthorizationRequest> requestEntity = new HttpEntity<>(request);
        ResponseEntity<AuthorizationResponse> responseEntity = restTemplate.exchange(
                "/authorization", 
                HttpMethod.PUT, 
                requestEntity, 
                AuthorizationResponse.class
        );

        // Verify
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        AuthorizationResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("DECLINED", response.getResponseCode()); // Adjust according to the expected scenario
        assertEquals(BigDecimal.ZERO, response.getBalance().getAmount()); // Adjust the expected value as needed
    }

}
