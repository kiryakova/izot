package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.service.OrderServiceModel;
import kiryakova.izot.domain.models.view.CustomerViewModel;
import kiryakova.izot.domain.models.view.OrderProductViewModel;
import kiryakova.izot.domain.models.view.OrderViewModel;
import kiryakova.izot.domain.models.view.ProductDetailsViewModel;
import kiryakova.izot.service.CustomerService;
import kiryakova.izot.service.OrderProductService;
import kiryakova.izot.service.OrderService;
import kiryakova.izot.service.ProductService;
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
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final CustomerService customerService;

    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(ProductService productService, OrderService orderService, OrderProductService orderProductService, CustomerService customerService, ModelMapper modelMapper) {
        this.productService = productService;
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = {"/product/details/{id}", "/product/details/{id}/{categoryId}"})
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsProductOrder(@PathVariable(name="id") String id, @PathVariable(name="categoryId", required=false) String categoryId, ModelAndView modelAndView) {
        modelAndView.addObject("product", this.modelMapper
                .map(this.productService
                        .findProductById(id), ProductDetailsViewModel.class));

        modelAndView.addObject("categoryId", categoryId);

        return this.view("order/product-details-order", modelAndView);
    }

    @GetMapping("/products/my")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView productsByOrder(Principal principal, ModelAndView modelAndView) throws Exception {
        String name = principal.getName();

        OrderServiceModel orderServiceModel = this.orderService.findUnfinishedOrderByUserName(name);

        if(orderServiceModel == null){
            modelAndView.addObject("order", null);
        }
        else {
            modelAndView.addObject("order", this.modelMapper.map(orderServiceModel, OrderViewModel.class));
        }

        modelAndView.addObject("orderedProducts", this.orderProductService
                .findOrderProductsByUser(name)
                .stream()
                .map(p -> this.modelMapper.map(p, OrderProductViewModel.class))
                .collect(Collectors.toList()));

        /*modelAndView.addObject("orderedProducts", this.modelMapper
                .map(this.orderProductService
                        .findOrderProductsByUser(name), OrderProductViewModel.class));*/

        return this.view("order/my-products", modelAndView);
    }

    @GetMapping("/confirm/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView confirmOrder(@PathVariable(name="id") String id, ModelAndView modelAndView) throws Exception {

        orderService.confirmOrder(id);
        //if(orderService.confirmOrder(id)) {



        /*
        modelAndView.addObject("order", this.modelMapper.map(this.orderService
                    .findOrderById(id), OrderViewModel.class));

        modelAndView.addObject("orderedProducts", this.orderProductService
                    .findOrderProductsByOrderId(id)
                    .stream()
                    .map(p -> this.modelMapper.map(p, OrderProductViewModel.class))
                    .collect(Collectors.toList()));

        modelAndView.addObject("customer", this.modelMapper.map(this.customerService
                .findCustomerByOrderId(id), CustomerViewModel.class));

        return this.view("order/confirmed-order", modelAndView);

        */

        return this.orderDetails(id, modelAndView);

        /*}
        else {

            modelAndView.addObject("order", this.modelMapper.map(this.orderService
                    .findUnfinishedOrderByUserName(name), OrderViewModel.class));

            modelAndView.addObject("orderedProducts", this.orderProductService
                    .findOrderProductsByUser(name)
                    .stream()
                    .map(p -> this.modelMapper.map(p, OrderProductViewModel.class))
                    .collect(Collectors.toList()));
            return this.view("order/my-products", modelAndView);
        }*/

    }


    @GetMapping("/all")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ModelAndView allOrders(ModelAndView modelAndView) {

        modelAndView.addObject("orders", this.orderService
                .findAllOrders()
                .stream()
                .map(o -> this.modelMapper.map(o, OrderViewModel.class))
                .collect(Collectors.toList()));

        return this.view("order/all-orders", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView orderDetails(@PathVariable(name="id") String id, ModelAndView modelAndView) {

        modelAndView.addObject("order", this.modelMapper.map(this.orderService
                .findOrderById(id), OrderViewModel.class));

        modelAndView.addObject("orderedProducts", this.orderProductService
                .findOrderProductsByOrderId(id)
                .stream()
                .map(p -> this.modelMapper.map(p, OrderProductViewModel.class))
                .collect(Collectors.toList()));

        modelAndView.addObject("customer", this.modelMapper.map(this.customerService
                .findCustomerByOrderId(id), CustomerViewModel.class));

        return this.view("order/confirmed-order", modelAndView);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView myOrders(Principal principal, ModelAndView modelAndView) throws Exception {
        String name = principal.getName();

        modelAndView.addObject("orders", this.orderService
                .findAllOrdersByUsername(name)
                .stream()
                .map(o -> this.modelMapper.map(o, OrderViewModel.class))
                .collect(Collectors.toList()));

        return this.view("order/all-orders", modelAndView);
    }
}
