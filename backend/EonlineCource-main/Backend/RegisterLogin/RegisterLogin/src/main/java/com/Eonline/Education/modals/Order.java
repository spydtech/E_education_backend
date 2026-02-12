package com.Eonline.Education.modals;

import com.Eonline.Education.user.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import okhttp3.Address;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Data
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_id")
    private String orderId;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @NotNull
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    @Embedded
   private PaymentDetails paymentDetails=new PaymentDetails();

    private double totalPrice;

    private Integer totalDiscountedPrice;

    private Integer discounte;

  //  private OrderStatus orderStatus;

    private int totalItem;

    private LocalDateTime createdAt;


}
