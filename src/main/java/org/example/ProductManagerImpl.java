package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;

public class ProductManagerImpl implements ProductManager{
    private Map<String ,List<Order>> ordersByUser;
    private Map<String, Product> products;
    private Map<String, User> users;
    private Queue<Order> pendingOrders;
    private static ProductManagerImpl instance;
    private static final Logger logger = LogManager.getLogger(ProductManagerImpl.class);

    private ProductManagerImpl() {
        this.ordersByUser = new HashMap<>();
        this.products = new HashMap<>();
        this.users = new HashMap<>();
        this.pendingOrders = new LinkedList<>();
    }

    public static ProductManagerImpl getInstance() {
        if (instance == null) {
            instance = new ProductManagerImpl();
        }
        return instance;
    }
    @Override
    public void addProduct(Product product) {
        logger.info("Se ha añadido un producto nuevo con id= {}",product.getId());
        products.put(product.getId(),product);
    }

    @Override
    public void addUser(User user) {
        logger.info("Se ha añadido un usuario con dni= {}",user.getDni());
        users.put(user.getDni(),user);
        ordersByUser.putIfAbsent(user.getDni(), new ArrayList<>());
    }

    @Override
    public List<Product> getProductsByPrice() {
        logger.info("Lista de productos ordenados por precios");
        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparingDouble(Product::getPrecio));
        return list;
    }

    @Override
    public Order placeOrder(String dni, List<OrderLine> pedido) throws UserNotFoundException, ProductNotFoundException {
        logger.info("placeOrder: dni = {}, lineas = {}", dni, pedido.size());
        if (!users.containsKey(dni)) {
            logger.error("El usuario con dni= {}, no existe",dni);
            throw new UserNotFoundException("El usuario " + dni + " no existe");
        }
        Order order = new Order(UUID.randomUUID().toString(), dni);
        for (OrderLine line : pedido) {
            if (!products.containsKey(line.getProductId())) {
                logger.error("El producto con id= {}, no existe",line.getProductId());
                throw new ProductNotFoundException("El producto " + line.getProductId() + " no existe");
            }
        }
        for (OrderLine line : pedido) {
            products.get(line.getProductId()).sumarVentas(line.getCantidad());
        }
        order.setLines(pedido);
        ordersByUser.get(dni).add(order);
        pendingOrders.add(order);
        logger.info("Se ha añadido al usuario con el dni= {}, el pedido {}", dni, order.getId());
        return order;
    }

    @Override
    public Order serveOrder() throws NoPendingOrdersException {
        Order first = pendingOrders.poll();
        if(first==null) {
            logger.error("La cola esta vacia");
            throw new NoPendingOrdersException("No hay nada que servir, la cola esta vacia.");
        }
        first.setServido(true);
        logger.info("La orden {} se ha servido con exito", first.getId());
        return first;
    }

    @Override
    public List<Order> getOrdersByUser(String dni) throws UserNotFoundException {
        logger.info("Lista de ordenes del usuario {}", dni);
        if (!users.containsKey(dni)) {
            logger.error("El usuario {} no existe", dni);
            throw new UserNotFoundException("El usuario " + dni + " no existe");
        }
        return ordersByUser.get(dni);
    }

    @Override
    public List<Product> getProductsBySales() {
        logger.info("Lista de productos ordenados por ventas");
        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparingInt(Product::getVentas).reversed());
        return list;
    }
    public void clear() {
        logger.info("clear: vaciando todas las estructuras");
        products.clear();
        users.clear();
        ordersByUser.clear();
        pendingOrders.clear();
    }
}
