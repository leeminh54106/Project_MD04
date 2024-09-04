package ra.project.model.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ra.project.constants.OrderStatus;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;

    private String serialNumber;

    private String username;

    private Long userId;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String receiveName;

    private String receiveAddress;

    private String receivePhone;

    private String note;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date receivedAt;

    private List<OrderDetailResponse> orderDetail;
}
