package com.zoneservice.zoneservice.Dto.Req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequest {
    private String name;
    private String zoneId;
}
