package ra.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project.model.entity.OrderDetails;
import ra.project.model.entity.Products;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findByProduct(Products product);
}

