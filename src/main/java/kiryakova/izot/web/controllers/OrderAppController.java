package kiryakova.izot.web.controllers;

import kiryakova.izot.service.OrderProductService;
import kiryakova.izot.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/fetch/order")
public class OrderAppController {
    private final OrderService orderService;

    @Autowired
    public OrderAppController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/add/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public boolean addOrder(@PathVariable(name="id") String id, Principal principal) throws Exception {
        String name = principal.getName();

        return this.orderService.addOrder(id, name);
    }

    @GetMapping("/product/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public BigDecimal deleteProductFromOrder(@PathVariable(name="id") String id) throws Exception {

        return this.orderService.deleteOrderProduct(id);
    }
}
