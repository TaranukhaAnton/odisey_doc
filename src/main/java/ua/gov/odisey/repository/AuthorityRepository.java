package ua.gov.odisey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.gov.odisey.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
