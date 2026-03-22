package com.zoneservice.zoneservice.Repository;

import com.zoneservice.zoneservice.Entity.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<ZoneEntity, Long> {
}
