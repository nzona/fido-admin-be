package it.be.izi.dog.repositories;

import it.be.izi.dog.entities.TrainingFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingFieldRepository extends JpaRepository<TrainingFieldEntity, Long> {
}
