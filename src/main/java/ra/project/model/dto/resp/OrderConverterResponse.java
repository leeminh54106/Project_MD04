package ra.project.model.dto.resp;

import ra.project.model.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderConverterResponse {
    public static OrderResponse changeOrderResponse(Order order) {
        List<OrderDetailResponse> products = order.getOrderDetails().stream()
                .map(orderDetails -> OrderDetailResponse.builder()
                        .productId(orderDetails.getProduct().getId())
                        .name(orderDetails.getProduct().getName())
                        .quantity(orderDetails.getOrderQuantity())
                        .unitPrice(orderDetails.getUnitPrice())
                        .build())
                .collect(Collectors.toList());
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUsers().getId())
                .serialNumber(order.getSerialNumber())
                .username(order.getUsers().getFullName())
                .note(order.getNote())
                .receiveAddress(order.getReceiveAddress())
                .receivePhone(order.getReceivePhone())
                .receiveName(order.getReceiveName())
                .totalPrice(order.getTotalPrice())
                .orderDetail(products)
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .receivedAt(order.getReceivedAt())
                .build();
    }
}