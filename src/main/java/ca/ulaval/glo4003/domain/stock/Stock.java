package ca.ulaval.glo4003.domain.stock;

public class Stock {
    private String title;
    private String name;
    private double value;

    public Stock(String title, String name) {
        this.title = title;
        this.name = name;
        this.value = 100.0;
    }

    public String getTitle() {
        return this.title;
    }

    public String getName() {
        return this.name;
    }

    public synchronized void updateValue(double variation) {
        this.value += variation;
    }

    // TODO: use MoneyAmount instead of double.
    public synchronized double getValue() {
        return this.value;
    }
}
