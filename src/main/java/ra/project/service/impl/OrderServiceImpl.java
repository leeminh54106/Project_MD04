package ra.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.project.constants.OrderStatus;
import ra.project.model.dto.req.OrderRequest;
import ra.project.model.dto.resp.OrderDetailResponse;
import ra.project.model.dto.resp.OrderResponse;
import ra.project.model.entity.*;
import ra.project.repository.IOrderDetailRepository;
import ra.project.repository.IOrderRepository;
import ra.project.repository.IProductsRepository;
import ra.project.repository.IShoppingCartRepository;
import ra.project.security.principle.MyUserDetails;
import ra.project.service.IOrderService;
import ra.project.service.IUserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final IOrderRepository orderRepository;

    private final IShoppingCartRepository cartRepository;

    private final IProductsRepository productRepository;

    private final IUserService userService;

    private final IOrderDetailRepository orderDetailRepository;


    @Override
    public Order orderNow(OrderRequest orderRequest) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ShoppingCart> shoppingCartsItems = cartRepository.findAllByUser(userDetails.getUsers());
        if(shoppingCartsItems.isEmpty()) {
            throw new NoSuchElementException("Giỏ hàng trống!");
        }

        Order order = Order.builder()
                .users(userDetails.getUsers())
                .serialNumber(UUID.randomUUID().toString())
                .totalPrice(calculateTotalPrice(shoppingCartsItems))
                .status(OrderStatus.WAITING)
                .receiveAddress(orderRequest.getReceiveAddress())
                .receivePhone(orderRequest.getReceivePhone())
                .receiveName(orderRequest.getReceiveName())
                .note(orderRequest.getNote())
                .createdAt(new Date())
                .receivedAt(addDays(new Date(), 4))
                .build();
        order = orderRepository.save(order);

        //Luu danh sách chi tiết đơn hàng
        Order findOder = order;
        List<OrderDetails> list = shoppingCartsItems.stream().map(
                item->{ Products product = item.getProduct();
                    if(item.getQuantity() > product.getQuantity()) {
                        throw new NoSuchElementException("Vượt quá số lượng trong kho!");
                    }
                    product.setQuantity(product.getQuantity() - item.getQuantity());
                    productRepository.save(product);
                    return OrderDetails.builder()
                            .order(findOder)
                            .product(product)
                            .name(product.getName())
                            .unitPrice(product.getPrice())
                            .orderQuantity(item.getQuantity())
                            .build();
                }).collect(Collectors.toList());
        List<OrderDetails> orderDetails = orderDetailRepository.saveAll(list);
        order.setOrderDetails(orderDetails);

        cartRepository.deleteAll(shoppingCartsItems);
        return order;
    }

    private Double calculateTotalPrice(List<ShoppingCart> shoppingCartItems) {
        return shoppingCartItems.stream().mapToDouble(item-> {
                    Products product = item.getProduct();
                    return product.getPrice() * item.getQuantity();
                })
                .sum();
    }

    private Date addDays(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderResponse> getOrderResponsesByStatus(OrderStatus status) {

        List<Order> orders = orderRepository.findByStatus(status);

        if(orders.isEmpty()) {
            throw new NoSuchElementException("Không có đơn hàng nào trong trạng thái: " + status);
        }

        return orders.stream().map(order -> OrderResponse.builder()
                .id(order.getId())
                .username(order.getUsers().getUsername())
                .userId(order.getUsers().getId())
                .serialNumber(order.getSerialNumber())
                .totalPrice(order.getTotalPrice())
                .receiveAddress(order.getReceiveAddress())
                .receivePhone(order.getReceivePhone())
                .receiveName(order.getReceiveName())
                .note(order.getNote())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .receivedAt(order.getReceivedAt())
                .orderDetail(order.getOrderDetails().stream().map(detail ->
                                OrderDetailResponse.builder()
                                        .productId(detail.getProduct().getId())
                                        .name(detail.getName())
                                        .unitPrice(detail.getUnitPrice())
                                        .quantity(detail.getOrderQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order =  orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại đơn hàng"));

        return OrderResponse.builder()
                .id(order.getId())
                .username(order.getUsers().getUsername())
                .userId(order.getUsers().getId())
                .serialNumber(order.getSerialNumber())
                .totalPrice(order.getTotalPrice())
                .receiveAddress(order.getReceiveAddress())
                .receivePhone(order.getReceivePhone())
                .receiveName(order.getReceiveName())
                .note(order.getNote())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .receivedAt(order.getReceivedAt())
                .orderDetail(
                        order.getOrderDetails().stream().map(detail ->
                                        OrderDetailResponse.builder()
                                                .productId(detail.getProduct().getId())
                                                .name(detail.getName())
                                                .unitPrice(detail.getUnitPrice())
                                                .quantity(detail.getOrderQuantity())
                                                .build())
                                .collect(Collectors.toList()))
                .build();
    }

    @Override
    public boolean updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại đơn hàng"));
        order.setStatus(status);
        orderRepository.save(order);
        return true;
    }

    @Override
    public List<Order> getAllUserOrders() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Order> orders = orderRepository.findAllByUsers(userDetails.getUsers());
        if (orders.isEmpty()) {
            throw new NoSuchElementException("Không có đơn hàng nào cho người dùng này.");
        }
        return orders;
    }

    @Override
    public Order getOrderBySerialNumber(String serialNumber) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findBySerialNumberAndUsers(serialNumber, userDetails.getUsers())
                .orElseThrow(() ->new NoSuchElementException("Không tồn tại đơn hàng với mã này"));
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus orderStatus) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Order> orders = orderRepository.findByStatusAndUsers(orderStatus, userDetails.getUsers());

        if (orders.isEmpty()) {
            throw new NoSuchElementException("Không tìm thấy đơn hàng nào với trạng thái: " + orderStatus);
        }
        return orders;
    }

    @Override
    public boolean cancelOrder(Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository.findByIdAndUsers(id, userDetails.getUsers())
                .orElseThrow(() -> new NoSuchElementException("Không tồn tại đơn hàng"));

        if (order.getStatus() == OrderStatus.WAITING) {
            for (OrderDetails orderDetail : order.getOrderDetails()) {
                Products product = orderDetail.getProduct();
                product.setQuantity(product.getQuantity() + orderDetail.getOrderQuantity());
                productRepository.save(product);
            }

            order.setStatus(OrderStatus.CANCEL);
            orderRepository.save(order);
            return true;
        }
        return false;
    }
    }


