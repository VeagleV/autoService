package org.application.autoService.repository;

import org.application.autoService.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhone(String phone);
    
    /**
     * Поиск клиентов по части email
     */
    @Query("SELECT c FROM Client c WHERE c.email LIKE CONCAT('%', :emailPattern, '%')")
    List<Client> findByEmailContaining(@Param("emailPattern") String emailPattern);
}
