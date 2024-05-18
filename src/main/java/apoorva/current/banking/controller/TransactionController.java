package apoorva.current.banking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import apoorva.current.banking.dto.AuthorizationRequest;
import apoorva.current.banking.dto.AuthorizationResponse;
import apoorva.current.banking.dto.LoadRequest;
import apoorva.current.banking.dto.LoadResponse;
import apoorva.current.banking.service.*;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping("/load")
    public ResponseEntity<LoadResponse> loadFunds(@RequestBody LoadRequest request) {
        LoadResponse response = transactionService.loadFunds(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/authorization")
    public ResponseEntity<AuthorizationResponse> authorizeTransaction(@RequestBody AuthorizationRequest request) {
        AuthorizationResponse response = transactionService.authorize(request);
        return ResponseEntity.status(201).body(response);
    }

    

}
