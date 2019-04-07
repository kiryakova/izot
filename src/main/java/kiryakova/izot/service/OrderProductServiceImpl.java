package kiryakova.izot.service;

import kiryakova.izot.domain.entities.OrderProduct;
import kiryakova.izot.domain.models.service.OrderProductServiceModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.OrderProductRepository;
import kiryakova.izot.validation.OrderProductValidation;
import kiryakova.izot.validation.UserValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderProductServiceImpl implements OrderProductService {
    private final OrderProductRepository orderProductRepository;
    private final UserService userService;
    private final UserValidationService userValidation;
    private final OrderProductValidation orderProductValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderProductServiceImpl(OrderProductRepository orderProductRepository, UserService userService, UserValidationService userValidation, OrderProductValidation orderProductValidation, ModelMapper modelMapper) {
        this.orderProductRepository = orderProductRepository;
        this.userService = userService;
        this.userValidation = userValidation;
        this.orderProductValidation = orderProductValidation;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderProductServiceModel findOrderProductByOrderIdAndProductId(String orderId, String productId) throws Exception {
        OrderProduct orderProduct = this.orderProductRepository
                .findOrderProductByOrderIdAndProductId(orderId, productId).orElse(null);

        if(orderProductValidation.isValid(orderProduct)) {
            return this.modelMapper.map(orderProduct, OrderProductServiceModel.class);
        }
        else{
            return null;
        }

    }

    @Override
    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProductRepository.save(orderProduct);
    }

    @Override
    public List<OrderProductServiceModel> findOrderProductsByUser(String username) throws Exception {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(username);
        if(!userValidation.isValid(userServiceModel)) {
            throw new Exception();
        }

        return  this.orderProductRepository
                .findOrderProductsByUser(userServiceModel.getId())
                .stream()
                .map(o -> this.modelMapper.map(o, OrderProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderProduct deleteOrderProduct(String id) throws Exception {

        OrderProduct orderProduct = this.orderProductRepository
                .findById(id)
                .orElse(null);

        if(!orderProductValidation.isValid(orderProduct)) {
            throw new Exception();
        }

        try {
            this.orderProductRepository.delete(orderProduct);
        } catch (Exception ignored) {
            //TODO: Fix this when discover exception type.
            return null;
        }

        return orderProduct;

    }

    @Override
    public List<OrderProductServiceModel> findOrderProductsByOrderId(String id) {
        return this.orderProductRepository.findOrderProductsByOrderId(id)
                .stream()
                .map(o -> this.modelMapper.map(o, OrderProductServiceModel.class))
                .collect(Collectors.toList());
    }
}
