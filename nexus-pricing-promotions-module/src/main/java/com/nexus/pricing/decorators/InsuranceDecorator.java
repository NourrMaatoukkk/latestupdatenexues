package com.nexus.pricing.decorators;

public class InsuranceDecorator extends PriceDecorator {

    public InsuranceDecorator(PriceComponent component) {
        super(component);
    }

    @Override
    public double getPrice() {
        return component.getPrice() + 100;
    }
}