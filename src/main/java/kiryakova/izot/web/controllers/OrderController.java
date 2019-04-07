package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.view.OrderProductViewModel;
import kiryakova.izot.domain.models.view.OrderViewModel;
import kiryakova.izot.domain.models.view.ProductDetailsViewModel;
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

    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(ProductService productService, OrderService orderService, OrderProductService orderProductService, ModelMapper modelMapper) {
        this.productService = productService;
        this.orderService = orderService;
        this.orderProductService = orderProductService;
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

        modelAndView.addObject("order", this.modelMapper.map(this.orderService
                .findUnfinishedOrderByUserName(name), OrderViewModel.class));

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
    public ModelAndView confirmOrder(Principal principal, @PathVariable(name="id") String id, ModelAndView modelAndView) throws Exception {

        if(orderService.confirmOrder(id)) {
            modelAndView.addObject("orderedProducts", this.orderProductService
                    .findOrderProductsByOrderId(id)
                    .stream()
                    .map(p -> this.modelMapper.map(p, OrderProductViewModel.class))
                    .collect(Collectors.toList()));

            return this.view("order/confirmed-order", modelAndView);
        }
        else {
            String name = principal.getName();
            modelAndView.addObject("orderedProducts", this.orderProductService
                    .findOrderProductsByUser(name)
                    .stream()
                    .map(p -> this.modelMapper.map(p, OrderProductViewModel.class))
                    .collect(Collectors.toList()));
            return this.view("order/my-products", modelAndView);
        }

    }

    /*
    @GetMapping("/all")
    //@PreAuthorize(value = "hasAuthority('ADMIN')")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ModelAndView allOrders(ModelAndView modelAndView) {
        Set<OrderViewModel> allOrdersViewModel = this
                .orderService
                .getAll()
                .stream()
                .map(x -> this.modelMapper.map(x, OrderViewModel.class))
                .collect(Collectors.toUnmodifiableSet());

        modelAndView.addObject("allOrders", allOrdersViewModel);

        return this.view("orders-all", modelAndView);
    }*/
}
