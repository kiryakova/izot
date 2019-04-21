package kiryakova.izot.web.controllers;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.models.binding.ProducerBindingModel;
import kiryakova.izot.domain.models.service.ProducerServiceModel;
import kiryakova.izot.domain.models.view.ProducerViewModel;
import kiryakova.izot.service.LogService;
import kiryakova.izot.service.ProducerService;
import kiryakova.izot.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/producers")
public class ProducerController extends BaseController {
    private final ProducerService producerService;
    private final LogService logService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProducerController(ProducerService producerService,
                              LogService logService,
                              ModelMapper modelMapper) {
        this.producerService = producerService;
        this.logService = logService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('MODERATOR')")
    @PageTitle("Добавяне на производител")
    public ModelAndView addCategory(ModelAndView modelAndView) {
        modelAndView.addObject("producer", new ProducerBindingModel());
        return this.view("producer/add-producer", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('MODERATOR')")
    @PageTitle("Добавяне на производител")
    public ModelAndView addProducer(Principal principal,
                                    ModelAndView modelAndView,
                                    @ModelAttribute(name = "producer") @Valid ProducerBindingModel producerBindingModel,
                                    BindingResult bindingResult) {

        if(this.producerService.checkIfProducerNameAlreadyExists(producerBindingModel.getName())){
            bindingResult.addError(
                    new FieldError("producerBindingModel", "name",
                    String.format(
                            ConstantsDefinition.ProducerConstants.PRODUCER_ALREADY_EXISTS,
                            producerBindingModel.getName()))
            );
        }
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("producer", producerBindingModel);
            return this.view("producer/add-producer", modelAndView);
        }

        ProducerServiceModel producerServiceModel = this.modelMapper
                .map(producerBindingModel, ProducerServiceModel.class);

        this.producerService.addProducer(producerServiceModel);

        this.logService.logAction(principal.getName(),
                String.format(
                        ConstantsDefinition.ProducerConstants.PRODUCER_ADDED_SUCCESSFUL,
                        producerServiceModel.getName())
        );

        return this.redirect("/producers/all");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    @PageTitle("Редактиране на производител")
    public ModelAndView editCategory(@PathVariable String id,
                                     ModelAndView modelAndView) {
        modelAndView.addObject("producer",
                this.modelMapper.map(this.producerService.findProducerById(id),
                        ProducerBindingModel.class)
        );

        modelAndView.addObject("producerId", id);

        return this.view("producer/edit-producer", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    @PageTitle("Редактиране на производител")
    public ModelAndView editProducer(ModelAndView modelAndView,
                                     @PathVariable String id,
                                     @ModelAttribute(name = "producer") @Valid ProducerBindingModel producerBindingModel,
                                     BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("producer", producerBindingModel);
            modelAndView.addObject("producerId", id);
            return this.view("producer/edit-producer", modelAndView);
        }

        this.producerService.editProducer(id, this.modelMapper
                .map(producerBindingModel, ProducerServiceModel.class));

        return this.redirect("/producers/all");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    @PageTitle("Изтриване на производител")
    public ModelAndView deleteCategory(@PathVariable String id,
                                       ModelAndView modelAndView) {
        modelAndView.addObject("producer",
                this.modelMapper.map(this.producerService.findProducerById(id),
                        ProducerViewModel.class)
        );

        modelAndView.addObject("producerId", id);

        return this.view("producer/delete-producer", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    @PageTitle("Изтриване на производител")
    public ModelAndView deleteProducer(@PathVariable String id) {
        this.producerService.deleteProducer(id);

        return this.redirect("/producers/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('MODERATOR')")
    @PageTitle("Всички производители")
    public ModelAndView allProducer(ModelAndView modelAndView) {
        modelAndView.addObject("producers",
                this.producerService.findAllProducers()
                .stream()
                .map(c -> this.modelMapper.map(c, ProducerViewModel.class))
                .collect(Collectors.toList()));

        return this.view("producer/all-producers", modelAndView);
    }
}
