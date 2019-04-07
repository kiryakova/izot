package kiryakova.izot.domain.models.service;

public class OrderProductServiceModel extends BaseServiceModel {
    private Integer quantity;
    private ProductServiceModel product;
    private OrderServiceModel order;

    public OrderProductServiceModel() {
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductServiceModel getProduct() {
        return product;
    }

    public void setProduct(ProductServiceModel product) {
        this.product = product;
    }

    public OrderServiceModel getOrder() {
        return order;
    }

    public void setOrder(OrderServiceModel order) {
        this.order = order;
    }
}
