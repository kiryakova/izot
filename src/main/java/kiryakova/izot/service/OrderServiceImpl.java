package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.*;
import kiryakova.izot.domain.models.service.*;
import kiryakova.izot.error.OrderNotFoundException;
import kiryakova.izot.error.OrderNotSavedException;
import kiryakova.izot.error.OrderProductNotDeletedException;
import kiryakova.izot.repository.OrderRepository;
import kiryakova.izot.validation.OrderValidationService;
import kiryakova.izot.validation.ProductValidationService;
import kiryakova.izot.validation.UserValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;
    private final UserService userService;
    private final ProductService productService;
    private final UserValidationService userValidation;
    private final ProductValidationService productValidation;
    private final OrderValidationService orderValidation;

    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderProductService orderProductService, UserService userService, ProductService productService, UserValidationService userValidation, ProductValidationService productValidation, OrderValidationService orderValidation, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderProductService = orderProductService;
        this.userService = userService;
        this.productService = productService;
        this.userValidation = userValidation;
        this.productValidation = productValidation;
        this.orderValidation = orderValidation;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean addOrder(String productId, String username) throws Exception {

        UserServiceModel userServiceModel = this.userService.findUserByUsername(username);
        if(!userValidation.isValid(userServiceModel)) {
            throw new IllegalArgumentException();
        }

        ProductServiceModel productServiceModel = this.productService.findProductById(productId);
        if(!productValidation.isValid(productServiceModel)) {
            throw new IllegalArgumentException();
        }

        Product product = this.modelMapper.map(productServiceModel, Product.class);

        Order order = this.orderRepository.findUnfinishedOrderByUserId(userServiceModel.getId()).orElse(null);

        if(order == null){
            order = new Order();

            order.setUser(this.modelMapper.map(userServiceModel, User.class));
            order.setFinished(false);
            order.setTotalPrice(product.getPrice().add(new BigDecimal(0.00)));
        }
        else {
            if(order.getTotalPrice() != null){
                order.setTotalPrice(product.getPrice().add(order.getTotalPrice()));
            }
            else {
                order.setTotalPrice(product.getPrice().add(new BigDecimal(0.00)));
            }

        }

        OrderProduct orderProduct = new OrderProduct();

        try {
            order = this.orderRepository.saveAndFlush(order);

            OrderProductServiceModel orderProductServiceModel = this.orderProductService.findOrderProductByOrderIdAndProductId(order.getId(), productId);

            if(orderProductServiceModel != null){
                orderProduct = this.modelMapper.map(orderProductServiceModel, OrderProduct.class);
                orderProduct.setQuantity(orderProduct.getQuantity() + 1);
            }
            else{
                orderProduct.setQuantity(1);
                orderProduct.setOrder(order);
                orderProduct.setProduct(product);
            }

            this.orderProductService.addOrderProduct(orderProduct);

        } catch (Exception ignored) {
            throw new OrderNotSavedException(ConstantsDefinition.OrderConstants.UNSUCCESSFUL_SAVED_ORDER);
        }

        return true;
    }

    @Override
    public List<OrderServiceModel> findAllOrders() {
        return this.orderRepository
                .findAllOrders()
                .stream()
                .map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderServiceModel> findAllOrdersByUsername(String username) {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(username);
        if(!userValidation.isValid(userServiceModel)) {
            throw new IllegalArgumentException();
        }

        return this.orderRepository
                .findAllOrdersByUserId(userServiceModel.getId())
                .stream()
                .map(x -> this.modelMapper.map(x, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void confirmOrder(String id) {
        Order order = this.orderRepository.findById(id).orElse(null);

        this.checkIfOrderFound(order);

        try {
            order.setFinished(true);
            order.setOrderDate(LocalDate.now());
            this.orderRepository.save(order);
        } catch (Exception ignored) {
            throw new OrderNotSavedException(ConstantsDefinition.OrderConstants.UNSUCCESSFUL_SAVED_ORDER);
        }
    }

    @Override
    public BigDecimal deleteOrderProduct(String id) {

        try {
            OrderProduct orderProduct = this.orderProductService.deleteOrderProduct(id);

            BigDecimal totalPrice = orderProduct.getOrder().getTotalPrice();
            BigDecimal productPrice = orderProduct.getProduct().getPrice();
            Integer quantity = orderProduct.getQuantity();
            BigDecimal productsPrice = productPrice.multiply(new BigDecimal(quantity));
            BigDecimal newTotalPrice = totalPrice.subtract(productsPrice);

            this.setNewTotalPrice(newTotalPrice, orderProduct.getOrder());

            return newTotalPrice;
        }catch (Exception ignored){
            throw new OrderProductNotDeletedException(ConstantsDefinition.OrderConstants.UNSUCCESSFUL_DELETE_PRODUCT_BY_ORDER);
        }

    }

    private void setNewTotalPrice(BigDecimal totalPrice, Order order) {
        order.setTotalPrice(totalPrice);
        try{
            this.orderRepository.save(order);
        } catch (Exception ignored) {
            throw new OrderNotSavedException(ConstantsDefinition.OrderConstants.UNSUCCESSFUL_SAVED_ORDER);
        }
    }

    @Override
    public OrderServiceModel findUnfinishedOrderByUserName(String username) {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(username);
        if(!userValidation.isValid(userServiceModel)) {
            throw new IllegalArgumentException();
        }

        Order order = this.orderRepository.findUnfinishedOrderByUserId(userServiceModel.getId()).orElse(null);
        if(order != null) {
            return this.modelMapper.map(order, OrderServiceModel.class);
        }
        else {
            return null;
        }
    }

    @Override
    public OrderServiceModel findOrderById(String id) {
        Order order = this.orderRepository.findById(id).orElse(null);

        this.checkIfOrderFound(order);

        return this.modelMapper.map(order, OrderServiceModel.class);

    }

    @Override
    public void setCustomerForOrder(String orderId, CustomerServiceModel customerServiceModel) {
        Order order = this.orderRepository.findById(orderId).orElse(null);

        this.checkIfOrderFound(order);

        order.setCustomer(this.modelMapper.map(customerServiceModel, Customer.class));
        try{
            this.orderRepository.save(order);
        } catch (Exception ignored) {
            throw new OrderNotSavedException(ConstantsDefinition.OrderConstants.UNSUCCESSFUL_SAVED_ORDER);
        }
    }

    private void checkIfOrderFound(Order order) {
        if(!orderValidation.isValid(order)) {
            throw new OrderNotFoundException(ConstantsDefinition.OrderConstants.NO_SUCH_ORDER);
        }
    }
}