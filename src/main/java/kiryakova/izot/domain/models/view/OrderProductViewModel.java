package kiryakova.izot.domain.models.view;

import java.math.BigDecimal;

public class OrderProductViewModel extends BaseViewModel {
    private Integer quantity;
    private ProductDetailsViewModel product;
    private BigDecimal price;
    private OrderViewModel order;

    public OrderProductViewModel() {
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductDetailsViewModel getProduct() {
        return product;
    }

    public void setProduct(ProductDetailsViewModel product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderViewModel getOrder() {
        return order;
    }

    public void setOrder(OrderViewModel order) {
        this.order = order;
    }
}