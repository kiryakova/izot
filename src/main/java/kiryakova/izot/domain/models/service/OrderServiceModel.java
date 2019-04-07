package kiryakova.izot.domain.models.service;

import kiryakova.izot.domain.entities.OrderProduct;
import kiryakova.izot.domain.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderServiceModel extends BaseServiceModel {

    private LocalDate orderDate;
    private boolean finalized;
    private BigDecimal totalPrice;
    private User user;
    //private List<OrderProduct> orderProductList;


    public OrderServiceModel() {
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
