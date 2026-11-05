package org.example;

public class OrderLine {

    private String productId;
    private int cantidad;

    public OrderLine() {
    }

    public OrderLine(String productId, int cantidad) {
        this.productId = productId;
        this.cantidad = cantidad;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}