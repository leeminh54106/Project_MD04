package ra.project.model.dto.req;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {

@NotBlank(message = "sku không được để trống")
private String sku= UUID.randomUUID().toString();

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 1,message = "Giá phải lớn hơn 0")
    private Double price;

    @NotNull(message = "số lượng không được để trống")
    @Min(value = 1,message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Ảnh không được để trống")
    private MultipartFile image;

    @NotNull(message = "createdAt not null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Created date must be in the past")
    private Date createdAt;

    @NotNull(message = "Expire date is empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Expire date must be in the future or present")
    private Date expireAt;

    private Long categoryId;
}
