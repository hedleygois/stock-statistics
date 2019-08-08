package com.hedley.statistics.service.stock;

import com.hedley.statistics.dto.StockDto;
import com.hedley.statistics.entity.Stock;
import rx.Completable;
import rx.Single;

import java.util.List;
import java.util.Optional;

public interface StockService {
    Completable addStock(StockDto stock);
    Completable deleteStock(String ticker);
    Single<List<Stock>> findAll();
    Single<Optional<Stock>> findByTicker(String ticker);
    Single<Optional<Stock>> findByCompany(String company);
    Single<List<Stock>> findByValue(Double price);
    Single<Double> ma100();
}
