package com.control_api.factory.entity;

import com.control_api.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public final class TestProductFactory {

    private static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public static ProductBuilder notebook() {
        return aProduct()
                .withName("Notebook")
                .withDescription("Notebook dell")
                .withPrice(new BigDecimal("1200.00"))
                .withQuantity(23);
    }

    public static ProductBuilder iphone() {
        return aProduct()
                .withName("Iphone")
                .withDescription("Iphone 17")
                .withPrice(new BigDecimal("5000.00"))
                .withQuantity(65);
    }

    public static ProductBuilder watch() {
        return aProduct()
                .withName("Watch")
                .withDescription("Smart Watch")
                .withPrice(new BigDecimal("1800.00"))
                .withQuantity(8);
    }

    public static ProductBuilder tv() {
        return aProduct()
                .withName("TV")
                .withDescription("Smart TV")
                .withPrice(new BigDecimal("6000.90"))
                .withQuantity(9);
    }

    public static ProductBuilder smartBulb() {
        return aProduct()
                .withName("Smart Bulb")
                .withDescription("Smart bulb")
                .withPrice(new BigDecimal("100.00"))
                .withQuantity(100);
    }

    public static ProductBuilder pcGamer() {
        return aProduct()
                .withName("PC Gamer")
                .withDescription("PC gamer")
                .withPrice(new BigDecimal("5000.00"))
                .withQuantity(2);
    }

    public static ProductBuilder earphone() {
        return aProduct()
                .withName("Earphone")
                .withDescription("JBL Earphone")
                .withPrice(new BigDecimal("89.99"))
                .withQuantity(9);
    }

    public static ProductBuilder headset() {
        return aProduct()
                .withName("Headset")
                .withDescription("Headset gamer")
                .withPrice(new BigDecimal("250.00"))
                .withQuantity(4);
    }

    public static ProductBuilder mouse() {
        return aProduct()
                .withName("Mouse")
                .withDescription("Mouse logitech")
                .withPrice(new BigDecimal("90.90"))
                .withQuantity(13);
    }

    public static ProductBuilder drawingTable() {
        return aProduct()
                .withName("Drawing Table")
                .withDescription("Drawing Table")
                .withPrice(new BigDecimal("270.00"))
                .withQuantity(7);
    }

    public static ProductBuilder dualShock4() {
        return aProduct()
                .withName("DualShock 4")
                .withDescription("PS4 controllersd")
                .withPrice(new BigDecimal("70.00"))
                .withQuantity(90);
    }

    public static ProductBuilder keyboard() {
        return aProduct()
                .withName("Keyboard")
                .withDescription("Gamer keyboard")
                .withPrice(new BigDecimal("71.00"))
                .withQuantity(12);
    }

    public static ProductBuilder hdmiCable() {
        return aProduct()
                .withName("HDMI Cable")
                .withDescription("HDMI Cable")
                .withPrice(new BigDecimal("8.90"))
                .withQuantity(33);
    }

    public static ProductBuilder laptopPowerAdapter() {
        return aProduct()
                .withName("Laptop Power Adapter")
                .withDescription("Labptop power adapter")
                .withPrice(new BigDecimal("16.50"))
                .withQuantity(5);
    }

    public static ProductBuilder xboxController() {
        return aProduct()
                .withName("Xbox Series S/X/One Controller")
                .withDescription("Xbox Series S/X/One Controller")
                .withPrice(new BigDecimal("69.96"))
                .withQuantity(9);
    }

    public static List<Product> defaultProductList() {
        return List.of(
                notebook().build(),
                iphone().build(),
                watch().build(),
                tv().build(),
                smartBulb().build(),
                pcGamer().build(),
                earphone().build(),
                headset().build(),
                mouse().build(),
                drawingTable().build(),
                dualShock4().build(),
                keyboard().build(),
                hdmiCable().build(),
                laptopPowerAdapter().build(),
                xboxController().build()
        );
    }

    public static class ProductBuilder {
        private String name = "Default Product";
        private String description = "Default Description";
        private BigDecimal price = new BigDecimal("100.00");
        private Integer quantity = 1;

        private ProductBuilder() {}

        public ProductBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder withPrice(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder withQuantity(final Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Product build() {
            return new Product(name, description, price, quantity);
        }
    }
}
