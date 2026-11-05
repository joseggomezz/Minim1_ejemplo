package org.example;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String id;
    private String userId;
    private List<OrderLine> lines;
    private boolean servido;

    public Order() {
        this.lines = new ArrayList<>();
        this.servido = false;
    }

    public Order(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.lines = new ArrayList<>();
        this.servido = false;
    }

    public void addLine(OrderLine line) {
        this.lines.add(line);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<OrderLine> getLines() { return lines; }
    public void setLines(List<OrderLine> lines) { this.lines = lines; }

    public boolean isServido() { return servido; }
    public void setServido(boolean servido) { this.servido = servido; }
}
