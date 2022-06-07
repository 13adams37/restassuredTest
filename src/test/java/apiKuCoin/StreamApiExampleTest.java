package apiKuCoin;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class StreamApiExampleTest extends Methods {

    @Test
    public void checkUSDTCrypto() {
        List<TickerData> USDTickers = getTickers().stream().filter(x -> x.getSymbol().endsWith("USDT")).collect(Collectors.toList());
        Assertions.assertTrue(USDTickers.stream().allMatch(x -> x.getSymbol().endsWith("USDT")));
    }

    @Test
    public void sortByHighTickers() {
        List<TickerData> sortedTickersByPrice = getTickers().stream().filter(x -> x.getSymbol().endsWith("USDT"))
                .sorted(new TickerComparatorHigh()).collect(Collectors.toList());
        List<TickerData> top10Tickers = sortedTickersByPrice.stream().limit(10).collect(Collectors.toList());
        Assertions.assertEquals(top10Tickers.get(0).getSymbol(), "BTC-USDT");

    }

    @Test
    public void sortByLowTickers() {
        List<TickerData> sortedTickersByPrice = getTickers().stream().filter(x -> x.getSymbol().endsWith("USDT"))
                .sorted(new TickerComparatorLow()).limit(10).collect(Collectors.toList());

    }

    @Test
    public void mapTest() {
//        Map<String, Float> usd = new HashMap<>();
//        List<String> lowerCases = getTickers().stream().map(x -> x.getSymbol().toLowerCase()).collect(Collectors.toList());
//        getTickers().forEach(x -> usd.put(x.getSymbol(), Float.parseFloat(x.getChangeRate())));
        List<TickerShortedData> shortList = new ArrayList<>();
        getTickers().forEach(x -> shortList.add(new TickerShortedData(x.getSymbol(), Float.parseFloat(x.getChangeRate()))));

    }
}
