package com.example.review.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class CreatedModifiedAuditing {

    @CreatedDate
    @Column(updatable = false)
    protected OffsetDateTime createdDate;

    @LastModifiedDate
    protected OffsetDateTime modifiedDate;

}
