package com.zoneservice.zoneservice.Client;

import com.zoneservice.zoneservice.Dto.Req.DeviceRequest;
import com.zoneservice.zoneservice.Dto.Req.LoginRequest;
import com.zoneservice.zoneservice.Dto.Res.DeviceResponse;
import com.zoneservice.zoneservice.Dto.Res.LoginResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class IoTClient {

    private final WebClient webClient;
    private String accessToken;

    public IoTClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://104.211.95.241:8080/api")
                .build();
    }

    private void authenticate() {

        LoginRequest request = new LoginRequest("username", "123456");

        LoginResponse response = webClient.post()
                .uri("/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .block();

        this.accessToken = response.getAccessToken();
    }

    public String registerDevice(String zoneName) {

        if (accessToken == null) {
            authenticate();
        }

        DeviceRequest request =
                new DeviceRequest("Sensor-" + zoneName, zoneName);

        DeviceResponse response = webClient.post()
                .uri("/devices")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DeviceResponse.class)
                .block();

        return response.getDeviceId();
    }
}