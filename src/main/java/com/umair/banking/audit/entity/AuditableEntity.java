package com.umair.banking.audit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by_user_id", updatable = false, nullable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by_user_id", updatable = false, nullable = false)
    private Long updatedBy;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }


}
