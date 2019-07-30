package com.hedley.statistics.entity;

import lombok.*;

import javax.persistence.*;

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

    @Column(name = "ticker", unique = true)
    private String ticker;

    @Column(name = "company", unique = true)
    private String company;

    @Column(name = "value")
    private Double value;

}
