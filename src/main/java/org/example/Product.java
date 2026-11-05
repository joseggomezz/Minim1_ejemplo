package org.example;

public class Product {

    private String id;
    private String nombre;
    private double precio;
    private int ventas;

    public Product() {
        this.ventas = 0;
    }

    public Product(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.ventas = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getVentas() { return ventas; }
    public void setVentas(int ventas) { this.ventas = ventas; }

    public void sumarVentas(int cantidad) {
        this.ventas += cantidad;
    }
}
