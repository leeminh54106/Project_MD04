package ra.project.service;

import ra.project.constants.OrderStatus;
import ra.project.model.dto.req.OrderRequest;
import ra.project.model.dto.resp.OrderResponse;
import ra.project.model.entity.Order;

import java.util.List;

public interface IOrderService {
    Order orderNow(OrderRequest orderRequest);
    List<Order> getAllOrders();
    List<OrderResponse> getOrderResponsesByStatus(OrderStatus status);
    OrderResponse getOrderById(Long id);
    boolean updateOrderStatus(Long id, OrderStatus status);
    List<Order> getAllUserOrders();
    Order getOrderBySerialNumber(String serialNumber);

    List<Order> getOrdersByStatus(OrderStatus orderStatus);

    boolean cancelOrder(Long id);
}