package com.zoneservice.zoneservice.Controller;

import com.zoneservice.zoneservice.Entity.ZoneEntity;
import com.zoneservice.zoneservice.Service.ZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/zones")
public class ZoneController {
    private final ZoneService zoneService;
    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public ResponseEntity<ZoneEntity> create(@RequestBody ZoneEntity zone) {
        return ResponseEntity.ok(zoneService.createZone(zone));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneEntity> get(@PathVariable Long id) {
        return ResponseEntity.ok(zoneService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ZoneEntity>> getAll() {
        return ResponseEntity.ok(zoneService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZoneEntity> update(@PathVariable Long id,
                                       @RequestBody ZoneEntity zone) {
        return ResponseEntity.ok(zoneService.update(id, zone));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
