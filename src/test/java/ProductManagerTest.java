package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ProductManagerTest {

    private ProductManagerImpl pm;

    @Before
    public void setUp() {
        pm = ProductManagerImpl.getInstance();
        pm.addProduct(new Product("P1", "Cafe", 1.50));
        pm.addProduct(new Product("P2", "Bocadillo", 3.00));
        pm.addProduct(new Product("P3", "Agua", 1.00));
        pm.addUser(new User("12345678A"));
    }

    @After
    public void tearDown() {
        pm.clear();
    }

    @Test
    public void testPlaceOrder() throws Exception {
        // 1. ejecutar la operación
        Order order = pm.placeOrder("12345678A", List.of(new OrderLine("P1", 2)));
        // 2. comprobar con assert...
        assertNotNull(order);;
        assertEquals("12345678A", order.getUserId());
        assertFalse(order.isServido());
        assertEquals(1, order.getLines().size());
        assertEquals(1, pm.getOrdersByUser("12345678A").size());
    }
    @Test
    public void testGetInstanceIsSingleton() {
        assertSame(pm, ProductManagerImpl.getInstance());
    }

    @Test
    public void testGetProductsBySales() throws Exception {
        pm.placeOrder("12345678A", List.of(new OrderLine("P1", 2), new OrderLine("P2", 5)));
        pm.placeOrder("12345678A", List.of(new OrderLine("P3", 1)));

        List<Product> lista = pm.getProductsBySales();

        assertEquals("Bocadillo", lista.get(0).getNombre());
        assertEquals("Cafe", lista.get(1).getNombre());
        assertEquals("Agua", lista.get(2).getNombre());
    }

    @Test(expected = UserNotFoundException.class)
    public void testPlaceOrderUserNotFound() throws Exception {
        pm.placeOrder("00000000X", List.of(new OrderLine("P1", 1)));
    }

    @Test
    public void testPlaceOrderProductNotFound() throws Exception {
        try {
            pm.placeOrder("12345678A", List.of(
                    new OrderLine("P1", 2),
                    new OrderLine("NOEXISTE", 1)));
            fail("Debía lanzar ProductNotFoundException");
        } catch (ProductNotFoundException e) {
            // es lo esperado
        }

        // No debe haberse creado el pedido ni sumado ninguna venta
        assertEquals(0, pm.getOrdersByUser("12345678A").size());
        for (Product p : pm.getProductsBySales()) {
            assertEquals(0, p.getVentas());
        }
    }


    @Test
    public void testServeOrder() throws Exception {
        Order order = pm.placeOrder("12345678A", List.of(new OrderLine("P1", 2)));

        Order servido = pm.serveOrder();

        assertNotNull(servido);
        assertEquals(order.getId(), servido.getId());
        assertTrue(servido.isServido());
    }

    @Test(expected = NoPendingOrdersException.class)
    public void testServeOrderEmptyQueue() throws Exception {
        pm.serveOrder();
    }

    @Test
    public void testGetOrdersByUser() throws Exception {
        pm.placeOrder("12345678A", List.of(new OrderLine("P1", 1)));
        pm.placeOrder("12345678A", List.of(new OrderLine("P2", 1)));
        pm.serveOrder();

        // Devuelve todos: el servido y el pendiente
        assertEquals(2, pm.getOrdersByUser("12345678A").size());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetOrdersByUserNotFound() throws Exception {
        pm.getOrdersByUser("00000000X");
    }


}
