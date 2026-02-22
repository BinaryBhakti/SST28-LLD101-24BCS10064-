import java.util.*;

public class DefaultAddOnPricing implements AddOnPricing {
    private final Map<AddOn, Double> prices = new HashMap<>();

    public DefaultAddOnPricing() {
        prices.put(AddOn.MESS, 1000.0);
        prices.put(AddOn.LAUNDRY, 500.0);
        prices.put(AddOn.GYM, 300.0);
    }

    public double price(AddOn addOn) {
        return prices.getOrDefault(addOn, 0.0);
    }
}
