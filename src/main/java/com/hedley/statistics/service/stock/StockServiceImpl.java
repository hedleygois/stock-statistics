package com.hedley.statistics.service.stock;

import com.google.common.collect.Lists;
import com.hedley.statistics.dto.StockDto;
import com.hedley.statistics.entity.Stock;
import com.hedley.statistics.functions.SimpleFunctionsObject;
import com.hedley.statistics.repository.StockRepository;
import io.vavr.Function1;
import io.vavr.control.Try;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;
import rx.Completable;
import rx.Single;

import java.util.List;
import java.util.Optional;
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
    public Single<Optional<Stock>> findByTicker(String ticker) {
        return Single.create(subscriber -> subscriber.onSuccess(Optional.ofNullable(this.repository.findByTicker(ticker))));
    }

    @Override
    public Single<Optional<Stock>> findByCompany(String company) {
        return Single.create(subscriber -> subscriber.onSuccess(Optional.ofNullable(this.repository.findByCompany(company))));
    }

    @Override
    public Single<List<Stock>> findByValue(Double price) {
        return Single.create(subscriber -> subscriber.onSuccess(Lists.newArrayList(this.repository.findByValue(price))));
    }

    private Function1<Void, List<Stock>> list = aVoid -> this.repository.findAll();

    private Function1<List<Stock>, Function1<Integer, Double>> curriedMovingAverage =
            SimpleFunctionsObject.movingAverage.curried();

    private Function1<Void, Function<Integer, Double>> ma = this.list.andThen(this.curriedMovingAverage);

    @Override
    public Single<Double> ma100() {
        return Single.create(subscriber -> subscriber.onSuccess(this.ma.apply(null).apply(100)));
    }

    private Stock convert(StockDto dto) {
        return Stock.builder().company(dto.getCompany()).ticker(dto.getTicker()).value(dto.getValue()).build();
    }
}
