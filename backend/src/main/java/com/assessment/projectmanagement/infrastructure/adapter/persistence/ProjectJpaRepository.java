package com.assessment.projectmanagement.infrastructure.adapter.persistence;

import com.assessment.projectmanagement.infrastructure.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for ProjectEntity
 */
@Repository
public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, UUID> {
    List<ProjectEntity> findByOwnerIdAndDeletedFalse(UUID ownerId);
}
