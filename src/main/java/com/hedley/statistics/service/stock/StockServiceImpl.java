package com.hedley.statistics.service.stock;

import com.google.common.collect.Lists;
import com.hedley.statistics.dto.StockDto;
import com.hedley.statistics.entity.Stock;
import com.hedley.statistics.repository.StockRepository;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;
import rx.Completable;
import rx.Single;

import java.util.List;
import java.util.function.Function;

@Service
public class StockServiceImpl implements StockService {

    private StockRepository repository;

    @Autowired
    public StockServiceImpl(StockRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable addStock(StockDto stock) {
        return Completable.create(subscriber ->
            Try.of(() -> this.repository.save(convert(stock))).fold(error -> {
                subscriber.onError(error);
                return null;
            }, stock1 -> {
                subscriber.onCompleted();
                return null;
            })
        );
    }

    private Lazy<Function<StockDto, Stock>> save = Lazy.of((StockDto stock) -> repository.save(convert(stock)));

    @Override
    public Completable deleteStock(String ticker) {
        return Completable.create(subscriber ->
                Try.of(() -> this.repository.deleteByTicker(ticker)).fold(error -> {
                    subscriber.onError(error);
                    return null;
                }, aVoid -> {
                    subscriber.onCompleted();
                    return null;
                })
        );
    }

    @Override
    public Single<List<Stock>> findAll() {
        return Single.create(subscriber -> subscriber.onSuccess(Lists.newArrayList(this.repository.findAll())));
    }

    @Override
    public Single<Option<Stock>> findByTicker(String ticker) {
        return Single.create(subscriber -> subscriber.onSuccess(Option.some(this.repository.findByTicker(ticker))));
    }

    @Override
    public Single<Option<Stock>> findByCompany(String company) {
        return Single.create(subscriber -> subscriber.onSuccess(Option.some(this.repository.findByCompany(company))));
    }

    @Override
    public Single<List<Stock>> findByValue(Double price) {
        return Single.create(subscriber -> subscriber.onSuccess(Lists.newArrayList(this.repository.findByValue(price))));
    }

    private Stock convert(StockDto dto) {
        return Stock.builder().company(dto.getCompany()).ticker(dto.getTicker()).value(dto.getValue()).build();
    }
}
