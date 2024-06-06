package com.example.demo.db.repository;


import com.example.demo.db.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("""
            SELECT o FROM OrderEntity o
            WHERE o.id = :id
            """)
    OrderEntity getOrderByID(@Param("id") long id);

    @Query("""
           SELECT o FROM OrderEntity o
           WHERE o.username = :username
           """)
    List<OrderEntity> findByUsername(@Param("username") String username);
}
