package com.hedley.statistics.controller;

import com.hedley.statistics.dto.StockDto;
import com.hedley.statistics.entity.Stock;
import com.hedley.statistics.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/stocks")
public class StockController {

    private StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<Void>> addStock(@RequestBody StockDto dto) {
        return this.stockService.addStock(dto).subscribeOn(Schedulers.io()).toSingle(() -> ResponseEntity.ok().build());
    }

    @GetMapping(
            value = "/ticker/{ticker}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<StockDto>> getByTicker(@PathVariable(value = "ticker") String ticker) {
        return this.stockService.findByTicker(ticker)
                .subscribeOn(Schedulers.io())
                .map(opt -> ResponseEntity.of(opt.map(this::toDto)));
    }

    @GetMapping(
            value = "/company/{company}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<StockDto>> getByCompany(@PathVariable(value = "company") String company) {
        return this.stockService.findByCompany(company)
                .subscribeOn(Schedulers.io())
                .map(opt -> ResponseEntity.of(opt.map(this::toDto)));
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<List<StockDto>>> getAllStocks() {
        return this.stockService.findAll()
                .subscribeOn(Schedulers.io())
                .map(list -> ResponseEntity.ok(list.stream().map(this::toDto).collect(Collectors.toList())));
    }

    @GetMapping(
            value = "/ma100/{ticker}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<Double>> getMA100(@PathVariable(value = "ticker") String ticker) {
        return this.stockService.ma100().subscribeOn(Schedulers.computation()).map(ResponseEntity::ok);
    }

    // vavr-json later on
    private StockDto toDto(Stock stock) {
        return StockDto.builder().company(stock.getCompany()).ticker(stock.getTicker()).value(stock.getValue()).build();
    }

}
