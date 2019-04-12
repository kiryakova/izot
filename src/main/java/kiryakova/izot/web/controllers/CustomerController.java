package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.binding.CustomerBindingModel;
import kiryakova.izot.domain.models.service.CustomerServiceModel;
import kiryakova.izot.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/customers")
public class CustomerController extends BaseController {
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editCustomer(Principal principal, @PathVariable String id, ModelAndView modelAndView) throws Exception {
        String name = principal.getName();

        CustomerServiceModel customerServiceModel = this.customerService.findCustomerByUsername(name);

        CustomerBindingModel customerBindingModel = this.modelMapper.map(customerServiceModel, CustomerBindingModel.class);

        modelAndView.addObject("customer", customerBindingModel);
        modelAndView.addObject("orderId", id);

        return this.view("customer/edit-customer", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editCustomerConfirm(Principal principal, ModelAndView modelAndView,
                                            @PathVariable(name="id") String id,
                                            @ModelAttribute(name = "customer") @Valid CustomerBindingModel customerBindingModel,
                                            BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("customer", customerBindingModel);
            modelAndView.addObject("orderId", id);
            return this.view("customer/edit-customer", modelAndView);
        }

        String name = principal.getName();

        CustomerServiceModel customerServiceModel = this.modelMapper.map(customerBindingModel, CustomerServiceModel.class);

        this.customerService.editCustomer(name, customerServiceModel, id);

        return this.redirect("/orders/confirm/" + id);
    }
}
