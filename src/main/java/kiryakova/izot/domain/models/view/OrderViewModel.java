package kiryakova.izot.domain.models.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderViewModel extends BaseViewModel {

    private LocalDateTime orderDateTime;
    private boolean isFinished;
    private BigDecimal totalPrice;
    private UserViewModel user;
    private CustomerViewModel customer;

    public OrderViewModel() {
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public UserViewModel getUser() {
        return user;
    }

    public void setUser(UserViewModel user) {
        this.user = user;
    }

    public CustomerViewModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerViewModel customer) {
        this.customer = customer;
    }

}
