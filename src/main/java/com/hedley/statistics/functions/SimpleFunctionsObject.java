package com.hedley.statistics.functions;

import com.hedley.statistics.entity.Stock;
import io.vavr.Function2;
import lombok.var;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// I know this is not Scala but lets call object :p
public class SimpleFunctionsObject {

    public static final Function<List<Stock>, Double> average = (final List<Stock> stocks) ->
            stocks.stream().mapToDouble(Stock::getValue).reduce(Double::sum).getAsDouble();

    public static final Function<List<Stock>, Double> max = (final List<Stock> stocks) ->
            stocks.stream().max(Comparator.comparing(Stock::getValue)).map(Stock::getValue).orElse(0.0);

    public static final Function<List<Stock>, Double> min = (final List<Stock> stocks) ->
            stocks.stream().min(Comparator.comparing(Stock::getValue)).map(Stock::getValue).orElse(0.0);

    public static final Function<List<Stock>, Double> stdDev = (final List<Stock> stocks) -> {
        // I could also foldLeft these doubles to a DescriptiveStatistics but they are far from Immutable
        Double[] values = stocks.stream().map(Stock::getValue).collect(Collectors.toList()).toArray(new Double[]{0.0});
        // That's how ridiculous Java can still be. In 2019 still having problems with boxing/unboxing
        return new DescriptiveStatistics(ArrayUtils.toPrimitive(values)).getStandardDeviation();
    };

    // MA-ish
    public static final Function2<List<Stock>, Integer, Double> movingAverage = (final List<Stock> stocks, final Integer days) -> {
        Double[] values = stocks.stream().map(Stock::getValue).collect(Collectors.toList()).toArray(new Double[]{0.0});
        // Lombok <3
        var stats = new DescriptiveStatistics(ArrayUtils.toPrimitive(values));
        stats.setWindowSize(days);
        return stats.getMean();
    };

}
