package ru.skqwk.elearningsystem.model.enumeration;

public enum MaterialType {
    HOMEWORK("Домашнее задание"),
    LESSON("Урок");

    private final String name;

    MaterialType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
