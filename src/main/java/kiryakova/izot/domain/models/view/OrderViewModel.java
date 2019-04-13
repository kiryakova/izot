package kiryakova.izot.domain.models.view;

import kiryakova.izot.domain.entities.Customer;
import kiryakova.izot.domain.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderViewModel extends BaseViewModel {

    private LocalDateTime orderDateTime;
    private boolean finalized;
    private BigDecimal totalPrice;
    private User user;
    private Customer customer;
    //private List<OrderProductViewModel> orderProductList;

    public OrderViewModel() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /*public List<OrderProductViewModel> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProductViewModel> orderProductList) {
        this.orderProductList = orderProductList;
    }
    */
}
