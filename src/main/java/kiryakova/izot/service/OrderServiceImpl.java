package kiryakova.izot.service;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.entities.OrderProduct;
import kiryakova.izot.domain.entities.Product;
import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.OrderProductServiceModel;
import kiryakova.izot.domain.models.service.OrderServiceModel;
import kiryakova.izot.domain.models.service.ProductServiceModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
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
            throw new Exception();
        }

        ProductServiceModel productServiceModel = this.productService.findProductById(productId);
        if(!productValidation.isValid(productServiceModel)) {
            throw new Exception();
        }

        Product product = this.modelMapper.map(productServiceModel, Product.class);

        Order order = this.orderRepository.findUnfinishedOrderByUserId(userServiceModel.getId()).orElse(null);

        //List<OrderProduct> orderProducts = new ArrayList<>();

        if(order == null){
            order = new Order();

            //User user = this.modelMapper.map(userServiceModel, User.class);
            User user = new User();
            user.setId(userServiceModel.getId());

            order.setUser(user);
            order.setFinished(false);
            order.setTotalPrice(product.getPrice().add(new BigDecimal(0.00)));
        }
        else {
            //order.setTotalPrice(order.getTotalPrice() != null ? product.getPrice().add(order.getTotalPrice()) : product.getPrice().add(new BigDecimal(0)));
            if(order.getTotalPrice() != null){
                order.setTotalPrice(product.getPrice().add(order.getTotalPrice()));
            }
            else {
                order.setTotalPrice(product.getPrice().add(new BigDecimal(0)));
            }

        }

        //OrderProduct orderProduct = this.modelMapper.map(productOrderServiceModel, OrderProduct.class);
        OrderProduct orderProduct = new OrderProduct();

        //orderProduct.setQuantity(orderProduct.getQuantity() == null ? 1 : (quantity + 1));
        //orderProduct.setProduct(product);

        //orderProducts.add(orderProduct);

        //order.setOrderProductList(orderProducts);

        try {
            order = this.orderRepository.saveAndFlush(order);
            //orderProduct.setOrder(order);


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
            //TODO: Fix this when discover exception type.
            return false;
        }

        return true;

    }

    @Override
    public List<OrderServiceModel> findAllOrders() {
        return this.orderRepository
                .findAll()
                .stream()
                .map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderServiceModel> findAllOrdersByUserId(String userId) {
        return this.orderRepository
                .findAllByUserId(userId)
                .stream()
                .map(x -> this.modelMapper.map(x, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean confirmOrder(String id) throws Exception {
        Order order = this.orderRepository.findById(id).orElse(null);

        if(!orderValidation.isValid(order)) {
            throw new Exception();
        }

        try {
            order.setFinished(true);
            order.setOrderDate(LocalDate.now());
            this.orderRepository.save(order);
        } catch (Exception ignored) {
            //TODO: Fix this when discover exception type.
            return false;
        }

        return true;
    }

    @Override
    public BigDecimal deleteOrderProduct(String id) throws Exception {
        OrderProduct orderProduct = this.orderProductService.deleteOrderProduct(id);
        BigDecimal totalPrice = orderProduct.getOrder().getTotalPrice();
        BigDecimal productPrice = orderProduct.getProduct().getPrice();
        Integer quantity = orderProduct.getQuantity();
        BigDecimal productsPrice = productPrice.multiply(new BigDecimal(quantity));
        BigDecimal newTotalPrice = totalPrice.subtract(productsPrice);

        this.setNewTotalPrice(newTotalPrice, orderProduct.getOrder());
        //orderProduct.getOrder().setTotalPrice(newTotalPrice);

        return newTotalPrice;
    }

    @Override
    public boolean setNewTotalPrice(BigDecimal totalPrice, Order order) {
        order.setTotalPrice(totalPrice);
        try{
            this.orderRepository.save(order);
        } catch (Exception ignored) {
            //TODO: Fix this when discover exception type.
            return false;
        }

        return true;
    }

    @Override
    public OrderServiceModel findUnfinishedOrderByUserName(String username) throws Exception {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(username);
        if(!userValidation.isValid(userServiceModel)) {
            throw new Exception();
        }

        Order order = this.orderRepository.findUnfinishedOrderByUserId(userServiceModel.getId()).orElse(null);
        if(order != null) {
            return this.modelMapper.map(order, OrderServiceModel.class);
        }
        else {
            return null;
        }
    }
}
