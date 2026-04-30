# Nexus Pricing & Promotions Module

Nexus Pricing & Promotions Module is a Java Spring Boot pricing dashboard integrated with the Inventory Module for dynamic product pricing, promotions, and final cost calculation using modern design patterns.

---

## How to Run

### Prerequisites

* Java 17+
* Maven
* Firebase service account file named `serviceAccountKey.json` in project root

### Start the Application

```bash
mvn -q -DskipTests compile
mvn spring-boot:run
```

### Open the App

Open in browser:

```text
http://localhost:8080/login.html
```

Then open **Pricing Engine** from the dashboard sidebar.

---

## Module Features

* Load products directly from Inventory Module
* Show original base price instantly
* Apply dynamic pricing strategies
* Add promotional services
* Calculate final price with tax
* Reset pricing form instantly
* Professional integrated dashboard UI

---

## Pricing Strategies

### Normal Pricing

Uses original product price.

### VIP Pricing

Applies 10% discount.

### Black Friday Pricing

Applies 20% discount.

---

## Promotions

### Gift Wrap

Adds +50 to total price.

### Insurance

Adds +100 to total price.

---

## Final Calculation Logic

1. Load selected product base price
2. Apply selected strategy discount
3. Add optional services
4. Apply 14% tax
5. Display final result

---

## File Guide

[`src/main/java/com/nexus/pricing/controller/PricingController.java`](src/main/java/com/nexus/pricing/controller/PricingController.java)
Application REST controller responsible for handling pricing requests from frontend UI.

[`src/main/java/com/nexus/pricing/controller/PricingModule.java`](src/main/java/com/nexus/pricing/controller/PricingModule.java)
Main pricing module startup and integration class.

[`src/main/java/com/nexus/pricing/manager/PricingManager.java`](src/main/java/com/nexus/pricing/manager/PricingManager.java)
Coordinates strategies, decorators, and final price generation.

[`src/main/java/com/nexus/pricing/models/Order.java`](src/main/java/com/nexus/pricing/models/Order.java)
Represents customer pricing order information.

[`src/main/java/com/nexus/pricing/strategies/PricingStrategy.java`](src/main/java/com/nexus/pricing/strategies/PricingStrategy.java)
Common interface for all pricing strategies.

[`src/main/java/com/nexus/pricing/strategies/NormalPricing.java`](src/main/java/com/nexus/pricing/strategies/NormalPricing.java)
Returns normal product price.

[`src/main/java/com/nexus/pricing/strategies/VIPPricing.java`](src/main/java/com/nexus/pricing/strategies/VIPPricing.java)
Returns discounted VIP price.

[`src/main/java/com/nexus/pricing/strategies/BlackFridayPricing.java`](src/main/java/com/nexus/pricing/strategies/BlackFridayPricing.java)
Returns Black Friday promotional price.

[`src/main/java/com/nexus/pricing/decorators/PriceComponent.java`](src/main/java/com/nexus/pricing/decorators/PriceComponent.java)
Base component for decorator pricing structure.

[`src/main/java/com/nexus/pricing/decorators/BasePrice.java`](src/main/java/com/nexus/pricing/decorators/BasePrice.java)
Base product price object.

[`src/main/java/com/nexus/pricing/decorators/PriceDecorator.java`](src/main/java/com/nexus/pricing/decorators/PriceDecorator.java)
Abstract decorator wrapper.

[`src/main/java/com/nexus/pricing/decorators/GiftWrapDecorator.java`](src/main/java/com/nexus/pricing/decorators/GiftWrapDecorator.java)
Adds gift wrapping service fee.

[`src/main/java/com/nexus/pricing/decorators/InsuranceDecorator.java`](src/main/java/com/nexus/pricing/decorators/InsuranceDecorator.java)
Adds insurance service fee.

[`src/main/java/com/nexus/pricing/singleton/GlobalConfigManager.java`](src/main/java/com/nexus/pricing/singleton/GlobalConfigManager.java)
Singleton class holding shared tax and pricing configuration.

[`src/main/resources/static/index.html`](src/main/resources/static/index.html)
Main dashboard UI containing Pricing Engine integration.

[`src/main/resources/static/style.css`](src/main/resources/static/style.css)
Shared styling for full dashboard interface.

---

## Design Patterns Used

### Singleton

Used to keep one shared instance for pricing configuration across the system.

* [`GlobalConfigManager`](src/main/java/com/nexus/pricing/singleton/GlobalConfigManager.java) stores tax rate and shared pricing settings.
* Ensures one reusable configuration object for all pricing operations.

### Strategy

Used to switch pricing logic dynamically without changing core code.

* [`PricingStrategy`](src/main/java/com/nexus/pricing/strategies/PricingStrategy.java) is the common interface.
* [`NormalPricing`](src/main/java/com/nexus/pricing/strategies/NormalPricing.java) applies standard pricing.
* [`VIPPricing`](src/main/java/com/nexus/pricing/strategies/VIPPricing.java) applies VIP discount pricing.
* [`BlackFridayPricing`](src/main/java/com/nexus/pricing/strategies/BlackFridayPricing.java) applies seasonal promotion pricing.

### Decorator

Used to add optional services to the base product price.

* [`PriceComponent`](src/main/java/com/nexus/pricing/decorators/PriceComponent.java) is the base component.
* [`BasePrice`](src/main/java/com/nexus/pricing/decorators/BasePrice.java) holds original product price.
* [`GiftWrapDecorator`](src/main/java/com/nexus/pricing/decorators/GiftWrapDecorator.java) adds gift wrap fee.
* [`InsuranceDecorator`](src/main/java/com/nexus/pricing/decorators/InsuranceDecorator.java) adds insurance fee.

### MVC Structure

Used to separate interface, logic, and request handling.

* [`index.html`](src/main/resources/static/index.html) handles frontend view.
* [`PricingController`](src/main/java/com/nexus/pricing/controller/PricingController.java) handles HTTP requests.
* [`PricingManager`](src/main/java/com/nexus/pricing/manager/PricingManager.java) processes business logic.

---

## SOLID Principles in the Module

### Single Responsibility Principle

Each class has one main responsibility.

* Strategies handle pricing rules only
* Decorators handle add-ons only
* Controller handles requests only

### Open/Closed Principle

System is open for extension without modifying old code.

* New pricing strategies can be added easily
* New promotions can be added easily

### Liskov Substitution Principle

Any pricing strategy can replace another through same interface.

### Interface Segregation Principle

Small focused interfaces used for pricing behavior.

### Dependency Inversion Principle

PricingManager depends on abstractions like `PricingStrategy`.

---

## Integration Notes

* Products are loaded from Inventory Module
* Pricing Engine works inside same dashboard
* Shared styling keeps module visually integrated
* Final system behaves as one logistics platform

---

## Author

Ahmed Alyasergy

Developer of Pricing & Promotions Module

---

## Future Enhancements

* Coupon codes
* Loyalty discounts
* Seasonal campaigns
* Multi-currency pricing
* Revenue analytics
* Smart AI promotions
