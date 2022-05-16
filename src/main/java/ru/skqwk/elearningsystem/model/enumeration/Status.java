package ru.skqwk.elearningsystem.model.enumeration;

public enum Status {
    NOT_STARTED("badge error small", "Не приступил"),
    COMPLETED("badge small", "Выполнил"),
    EXCELLENT("badge success small", "5"),
    GOOD("badge small", "4"),
    NOT_BAD("badge error small", "3"),
    LEARNED("badge success small", "Изучил");

    private final String style;
    private final String name;

    Status(String style, String name) {
        this.style = style;
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public String getName() {
        return name;
    }
}
