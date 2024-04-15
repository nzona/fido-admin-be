package it.be.fido.admin.repositories;

import it.be.fido.admin.entities.TrainingFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingFieldRepository extends JpaRepository<TrainingFieldEntity, Long> {
}
