package com.api.recipe.common.entity;

import com.api.recipe.common.util.ConstantUtil;
import com.api.recipe.common.util.DateUtil;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@MappedSuperclass
public class BaseEntity {
    public static class Fields {
        public static final String ID = "id";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String MODIFIED_BY = "modified_by";

        private Fields() {
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1)
    @Column(name = Fields.ID)
    private Long id;

    @Column(name = Fields.CREATED_AT, nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = Fields.UPDATED_AT, nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = Fields.MODIFIED_BY, nullable = false)
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
