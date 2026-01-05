package com.assessment.projectmanagement.infrastructure.adapter.persistence;

import com.assessment.projectmanagement.infrastructure.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for TaskEntity
 */
@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID> {
    List<TaskEntity> findByProjectIdAndDeletedFalse(UUID projectId);

    long countByProjectIdAndDeletedFalse(UUID projectId);
}
