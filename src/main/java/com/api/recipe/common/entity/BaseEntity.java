package com.api.recipe.common.entity;

import com.api.recipe.common.util.ConstantUtil;
import com.api.recipe.common.util.DateUtil;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @SuppressWarnings(ConstantUtil.UNUSED_WARNING)
    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = DateUtil.nowUtc();
        this.createdAt = now;
        this.updatedAt = now;
        this.modifiedBy = ConstantUtil.SYSTEM_DELIMITER;
    }

    @SuppressWarnings(ConstantUtil.UNUSED_WARNING)
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = DateUtil.nowUtc();
        if (this.modifiedBy == null) {
            this.modifiedBy = ConstantUtil.SYSTEM_DELIMITER;
        }
    }
}
