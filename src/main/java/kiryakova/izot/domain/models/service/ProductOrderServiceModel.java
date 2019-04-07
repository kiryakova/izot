package kiryakova.izot.domain.models.service;

public class ProductOrderServiceModel {
    private Integer quantity;
    private ProductServiceModel product;
    private OrderServiceModel order;

    public ProductOrderServiceModel() {
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
