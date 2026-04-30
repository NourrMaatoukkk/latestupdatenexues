package com.nexus.pricing.decorators;

public class GiftWrapDecorator extends PriceDecorator {

    public GiftWrapDecorator(PriceComponent component) {
        super(component);
    }

    @Override
    public double getPrice() {
        return component.getPrice() + 50;
    }
}