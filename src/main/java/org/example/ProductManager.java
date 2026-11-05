package org.example;

import java.util.List;

public interface ProductManager {
    void addProduct(Product product);
    void addUser(User user);
    List<Product> getProductsByPrice();
    Order placeOrder(String dni, List<OrderLine> pedido)
            throws UserNotFoundException, ProductNotFoundException;
    Order serveOrder()throws NoPendingOrdersException;
    List<Order> getOrdersByUser(String dni)throws UserNotFoundException;
    List<Product> getProductsBySales();
}