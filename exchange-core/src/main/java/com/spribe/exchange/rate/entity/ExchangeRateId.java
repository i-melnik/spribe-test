package com.spribe.exchange.rate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ExchangeRateId implements Serializable {

    @Column(name = "base")
    private String base;

    @Column(name = "date")
    private ZonedDateTime date;
}
