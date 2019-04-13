package kiryakova.izot.web.controllers;

import kiryakova.izot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    //@GetMapping("/add/{id}{quantity}")
    @RequestMapping(
            value = "/add/",
            params = { "id", "quantity" },
            method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public boolean addOrder(@RequestParam("id") String id, @RequestParam("quantity") int quantity, Principal principal) throws Exception {
        String name = principal.getName();

        return this.orderService.addOrder(id, name, quantity);
    }

    @GetMapping("/product/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    //@ResponseBody
    public BigDecimal deleteProductFromOrder(@PathVariable(name="id") String id) throws Exception {

        return this.orderService.deleteOrderProduct(id);
    }
}
