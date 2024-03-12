package com.spribe.exchange.rate.entity;

import com.spribe.exchange.common.entity.BaseEntity;
import com.spribe.exchange.currency.entity.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"base"})
@Entity
@Table(name = "exchange_rate")
public class ExchangeRate extends BaseEntity {

    @EmbeddedId
    private ExchangeRateId id;

    @ManyToOne
    @JoinColumn(name = "base", insertable = false, updatable = false)
    @MapsId("base")
    private Currency base;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, BigDecimal> rates;

}
