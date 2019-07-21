package com.oldlie.zshop.zshopvue.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(updatable = false)
    @CreationTimestamp
    protected Date createDate;
    @UpdateTimestamp
    protected Date updateDate;

}
