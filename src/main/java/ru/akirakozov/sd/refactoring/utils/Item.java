package ru.akirakozov.sd.refactoring.utils;

public class Item {
    String name;
    long price;

    public Item(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
