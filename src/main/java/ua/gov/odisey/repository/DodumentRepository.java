package ua.gov.odisey.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ua.gov.odisey.domain.Dodument;

/**
 * Spring Data JPA repository for the Dodument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DodumentRepository extends JpaRepository<Dodument, Long> {}
