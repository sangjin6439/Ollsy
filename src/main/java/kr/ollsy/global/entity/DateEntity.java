package kr.ollsy.global.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class DateEntity {

    @Column(name = "CREATE_AT", updatable = false)
    @CreatedDate
    private LocalDateTime createAt;

    @Column(name = "UPDATE_AT")
    @LastModifiedDate
    private LocalDateTime updateAt;
}