package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.view.ProducerViewModel;
import kiryakova.izot.service.ProducerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProducerAppController {
    private final ProducerService producerService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProducerAppController(ProducerService producerService,
                                 ModelMapper modelMapper) {
        this.producerService = producerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/producers/fetch")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public List<ProducerViewModel> fetchProducers() {
        return this.producerService.findAllProducers()
                .stream()
                .map(c -> this.modelMapper.map(c, ProducerViewModel.class))
                .collect(Collectors.toList());
    }
}
