package org.example.services;

import org.example.NoPendingOrdersException;
import org.example.Order;
import org.example.Product;
import org.example.ProductManager;
import org.example.ProductManagerImpl;
import org.example.ProductNotFoundException;
import org.example.User;
import org.example.UserNotFoundException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/shop")
public class ProductManagerServices {

    private ProductManager pm;

    public ProductManagerServices() {
        this.pm = ProductManagerImpl.getInstance();
        // Datos de ejemplo, solo la primera vez
        if (pm.getProductsByPrice().isEmpty()) {
            pm.addProduct(new Product("P1", "Cafe", 1.50));
            pm.addProduct(new Product("P2", "Bocadillo", 3.00));
            pm.addProduct(new Product("P3", "Agua", 1.00));
            pm.addUser(new User("12345678A"));
        }
    }

    @GET
    @Path("/products/price")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByPrice() {
        List<Product> products = pm.getProductsByPrice();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @Path("/products/sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsBySales() {
        List<Product> products = pm.getProductsBySales();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @Path("/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrder(Order order) {
        if (order == null || order.getUserId() == null || order.getLines() == null
                || order.getLines().isEmpty()) {
            return Response.status(400).build();
        }
        try {
            Order created = pm.placeOrder(order.getUserId(), order.getLines());
            return Response.status(201).entity(created).build();
        } catch (UserNotFoundException | ProductNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @PUT
    @Path("/orders/serve")
    @Produces(MediaType.APPLICATION_JSON)
    public Response serveOrder() {
        try {
            Order served = pm.serveOrder();
            return Response.status(200).entity(served).build();
        } catch (NoPendingOrdersException e) {
            return Response.status(404).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/users/{dni}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersByUser(@PathParam("dni") String dni) {
        try {
            List<Order> orders = pm.getOrdersByUser(dni);
            GenericEntity<List<Order>> entity = new GenericEntity<List<Order>>(orders) {};
            return Response.status(200).entity(entity).build();
        } catch (UserNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
    }
}