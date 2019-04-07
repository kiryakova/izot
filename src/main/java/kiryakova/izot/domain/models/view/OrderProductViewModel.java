package kiryakova.izot.domain.models.view;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.entities.Product;
import kiryakova.izot.domain.models.service.OrderServiceModel;
import kiryakova.izot.domain.models.service.ProductServiceModel;

public class OrderProductViewModel extends BaseViewModel {
    private Integer quantity;
    private ProductServiceModel product;
    private OrderServiceModel order;

    public OrderProductViewModel() {
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
