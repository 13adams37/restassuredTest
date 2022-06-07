package apiKuCoin;

public class TickerShortedData {
    private String name;
    private Float changeRate;

    public TickerShortedData() {
    }

    public TickerShortedData(String name, Float changeRate) {
        this.name = name;
        this.changeRate = changeRate;
    }

    public String getName() {
        return name;
    }

    public Float getChangeRate() {
        return changeRate;
    }

}
