package com.hedley.statistics.service.stock;

import com.hedley.statistics.dto.StockDto;
import com.hedley.statistics.entity.Stock;
import io.vavr.control.Option;
import rx.Completable;
import rx.Single;

import java.util.List;

public interface StockService {
    Completable addStock(StockDto stock);
    Completable deleteStock(String ticker);
    Single<List<Stock>> findAll();
    Single<Option<Stock>> findByTicker(String ticker);
    Single<Option<Stock>> findByCompany(String company);
    Single<List<Stock>> findByValue(Double price);
}
