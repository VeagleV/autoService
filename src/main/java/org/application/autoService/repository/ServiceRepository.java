package org.application.autoService.repository;

import org.application.autoService.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    /**
     * Топ самых дорогих услуг
     */
    @Query(value = "SELECT * FROM service ORDER BY price DESC LIMIT :limit", nativeQuery = true)
    List<Service> findTopByOrderByPriceDesc(@Param("limit") int limit);
}
