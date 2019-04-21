package kiryakova.izot.domain.models.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderServiceModel extends BaseServiceModel {

    private LocalDateTime orderDateTime;
    private boolean finalized;
    private BigDecimal totalPrice;
    private UserServiceModel user;
    private CustomerServiceModel customer;

    public OrderServiceModel() {
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
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

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    public CustomerServiceModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerServiceModel customer) {
        this.customer = customer;
    }
}
