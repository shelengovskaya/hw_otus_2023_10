package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage copy() {
        ObjectForMessage duplicate = new ObjectForMessage();
        data = getData();
        if (data != null) {
            duplicate.setData(new ArrayList<>(data));
        }
        return duplicate;
    }
}
