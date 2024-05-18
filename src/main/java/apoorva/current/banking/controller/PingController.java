package apoorva.current.banking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController

public class PingController {

    @GetMapping("/ping")

    public ResponseEntity<PingResponse> ping() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDateTime = now.format(formatter);

        // Create and return the ping response
        PingResponse pingResponse = new PingResponse(formattedDateTime);
        return ResponseEntity.ok(pingResponse);
    }

    // Nested static class for the ping response body
    public static class PingResponse {
        private String serverTime;

        public PingResponse(String serverTime) {
            this.serverTime = serverTime;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }
    }
}