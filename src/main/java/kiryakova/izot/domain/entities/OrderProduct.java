package kiryakova.izot.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "order_products")
public class OrderProduct extends BaseEntity {
    private Integer quantity;
    private Product product;
    private Order order;

    public OrderProduct() {
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id",referencedColumnName = "id", nullable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(targetEntity = Order.class)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
