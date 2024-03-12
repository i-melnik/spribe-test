package com.spribe.exchange.currency.entity;

import com.spribe.exchange.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "currency")
public class Currency extends BaseEntity {

    @Id
    private String code;

    private String name;
}
