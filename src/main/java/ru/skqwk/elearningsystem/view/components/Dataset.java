package ru.skqwk.elearningsystem.view.components;

import java.util.List;

public class Dataset {
    private List<String> columns;
    private List<Row> rows;

    public Dataset(List<String> columns, List<Row> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }
}
