package com.api.recipe.common.repository;

import com.api.recipe.common.entity.BaseEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
@Transactional
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
}
