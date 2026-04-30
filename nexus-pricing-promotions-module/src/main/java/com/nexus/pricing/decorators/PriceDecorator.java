package com.nexus.pricing.decorators;

public abstract class PriceDecorator implements PriceComponent {

    protected PriceComponent component;

    public PriceDecorator(PriceComponent component) {
        this.component = component;
    }
}