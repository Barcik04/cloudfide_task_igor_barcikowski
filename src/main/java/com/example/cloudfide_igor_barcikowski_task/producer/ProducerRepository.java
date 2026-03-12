package com.example.cloudfide_igor_barcikowski_task.producer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {
    boolean existsByName(String name);
}
