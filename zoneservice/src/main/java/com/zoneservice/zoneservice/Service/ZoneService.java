package com.zoneservice.zoneservice.Service;

import com.zoneservice.zoneservice.Client.IoTClient;
import com.zoneservice.zoneservice.Entity.ZoneEntity;
import com.zoneservice.zoneservice.Repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final IoTClient ioTClient;

    public ZoneService(ZoneRepository zoneRepository, IoTClient ioTClient) {
        this.zoneRepository = zoneRepository;
        this.ioTClient = ioTClient;
    }

    // ✅ CREATE ZONE + REGISTER DEVICE
    public ZoneEntity createZone(ZoneEntity zone) {

        // 🔴 Validation (IMPORTANT from PDF)
        if (zone.getMinTemp() >= zone.getMaxTemp()) {
            throw new RuntimeException("minTemp must be less than maxTemp");
        }

        // 🟡 Call IoT API to register device
        String deviceId = ioTClient.registerDevice(zone.getName());

        // 🟢 Save deviceId into zone
        zone.setDeviceId(deviceId);

        return zoneRepository.save(zone);
    }

    // ✅ GET ZONE BY ID
    public ZoneEntity getById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
    }

    // ✅ GET ALL ZONES
    public List<ZoneEntity> getAll() {
        return zoneRepository.findAll();
    }

    // ✅ UPDATE ZONE
    public ZoneEntity update(Long id, ZoneEntity updatedZone) {

        ZoneEntity existing = getById(id);

        // 🔴 Validation again
        if (updatedZone.getMinTemp() >= updatedZone.getMaxTemp()) {
            throw new RuntimeException("Invalid temperature range");
        }

        existing.setName(updatedZone.getName());
        existing.setMinTemp(updatedZone.getMinTemp());
        existing.setMaxTemp(updatedZone.getMaxTemp());

        return zoneRepository.save(existing);
    }

    // ✅ DELETE ZONE
    public void delete(Long id) {
        ZoneEntity zone = getById(id);
        zoneRepository.delete(zone);
    }
}