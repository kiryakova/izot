package kiryakova.izot.web.controllers;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.models.service.OrderServiceModel;
import kiryakova.izot.domain.models.view.CustomerViewModel;
import kiryakova.izot.domain.models.view.OrderProductViewModel;
import kiryakova.izot.domain.models.view.OrderViewModel;
import kiryakova.izot.service.*;
import kiryakova.izot.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final CustomerService customerService;
    private final LogService logService;

    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService,
                           OrderProductService orderProductService,
                           CustomerService customerService,
                           LogService logService,
                           ModelMapper modelMapper) {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.customerService = customerService;
        this.logService = logService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/cancel/order/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Отказ поръчка")
    public ModelAndView cancelOrder(Principal principal,
                                    @PathVariable(name="id") String id,
                                    ModelAndView modelAndView) throws Exception {

        this.orderService.cancelOrder(id);

        this.logService.logAction(principal.getName(),
                ConstantsDefinition.OrderConstants.ORDER_CANCELED_SUCCESSFUL);

        return this.redirect("/orders/products/my");

    }

    @GetMapping("/all")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PageTitle("Всички поръчки")
    public ModelAndView allOrders(ModelAndView modelAndView) {

        modelAndView.addObject("orders",
                this.orderService.findAllOrders()
                        .stream()
                        .map(o -> this.modelMapper.map(o, OrderViewModel.class))
                        .collect(Collectors.toList()));

        return this.view("order/all-orders", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Поръчка")
    public ModelAndView orderDetails(@PathVariable(name="id") String id,
                                     ModelAndView modelAndView) {

        modelAndView.addObject("order",
                this.modelMapper.map(this.orderService
                        .findOrderById(id), OrderViewModel.class));

        modelAndView.addObject("orderedProducts",
                this.orderProductService.findOrderProductsByOrderId(id)
                        .stream()
                        .map(p -> this.modelMapper.map(p, OrderProductViewModel.class))
                        .collect(Collectors.toList()));

        modelAndView.addObject("customer",
                this.modelMapper.map(this.customerService
                        .findCustomerByOrderId(id), CustomerViewModel.class));

        return this.view("order/details-order", modelAndView);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Моите поръчки")
    public ModelAndView myOrders(Principal principal,
                                 ModelAndView modelAndView) throws Exception {

        String name = principal.getName();

        modelAndView.addObject("orders",
                this.orderService.findAllOrdersByUsername(name)
                        .stream()
                        .map(o -> this.modelMapper.map(o, OrderViewModel.class))
                        .collect(Collectors.toList()));

        return this.view("order/my-orders", modelAndView);
    }

    @GetMapping("/products/my")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Заявени продукти")
    public ModelAndView productsByOrder(Principal principal,
                                        ModelAndView modelAndView) throws Exception {

        String name = principal.getName();

        OrderServiceModel orderServiceModel = this.orderService
                .findUnfinishedOrderByUserName(name);

        if(orderServiceModel == null){
            modelAndView.addObject("order", null);
        }
        else {
            modelAndView.addObject("order",
                    this.modelMapper.map(orderServiceModel, OrderViewModel.class));
        }

        modelAndView.addObject("orderedProducts",
                this.orderProductService.findOrderProductsByUser(name)
                .stream()
                .map(p -> this.modelMapper.map(p, OrderProductViewModel.class))
                .collect(Collectors.toList()));

        return this.view("order/my-ordered-products", modelAndView);
    }

    @GetMapping("/confirm/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Потвърдена поръчка")
    public ModelAndView confirmOrder(Principal principal,
                                     @PathVariable(name="id") String id,
                                     ModelAndView modelAndView) throws Exception {

        this.orderService.confirmOrder(id);

        this.logService.logAction(principal.getName(),
                ConstantsDefinition.OrderConstants.ORDER_CONFIRMED_SUCCESSFUL);

        modelAndView.addObject("order",
                this.modelMapper.map(this.orderService
                        .findOrderById(id), OrderViewModel.class));

        modelAndView.addObject("orderedProducts",
                this.orderProductService.findOrderProductsByOrderId(id)
                        .stream()
                        .map(p -> this.modelMapper.map(p, OrderProductViewModel.class))
                        .collect(Collectors.toList()));

        modelAndView.addObject("customer",
                this.modelMapper.map(this.customerService
                        .findCustomerByOrderId(id), CustomerViewModel.class));

        return this.view("order/confirmed-order", modelAndView);

    }

}
