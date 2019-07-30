package com.hedley.statistics.repository;

import com.hedley.statistics.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByTicker(String ticker);
    List<Stock> findByValue(Double price);
    Stock findByCompany(String company);
    Void deleteByTicker(String ticker);
}
