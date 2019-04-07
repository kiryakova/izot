package kiryakova.izot.domain.entities;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private LocalDate orderDate;
    private boolean isFinished;
    private User user;
    private BigDecimal totalPrice;
    //private User performer;
    private List<OrderProduct> orderProductList;

    public Order() {
    }

    @NonNull
    @Column(name = "order_date")
    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    @Column(name = "is_finished")
    public boolean isFinished() {
        return this.isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

/*
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "performer_id", referencedColumnName = "id")
    public User getPerformer() {
        return performer;
    }

    public void setPerformer(User performer) {
        this.performer = performer;
    }
*/
    @OneToMany(targetEntity = OrderProduct.class, mappedBy = "order")
    public List<OrderProduct> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }

    @Column(name = "total_price")
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
