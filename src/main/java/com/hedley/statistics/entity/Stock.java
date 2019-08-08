package com.hedley.statistics.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Stocks", indexes = { @Index(name = "ticker_idx", columnList = "ticker") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "company")
    private String company;

    @Column(name = "value")
    private Double value;

    @Column(name = "period")
    private LocalDate period;

}
