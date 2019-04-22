package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.view.LogViewModel;
import kiryakova.izot.service.LogService;
import kiryakova.izot.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LogController extends BaseController {

    private final LogService logService;
    private final ModelMapper modelMapper;

    @Autowired
    public LogController(LogService logService, ModelMapper modelMapper) {
        this.logService = logService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/logs")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("Логове")
    public ModelAndView logs(ModelAndView modelAndView) {

        List<LogViewModel> logs = this.logService.findAllLogsByDateTimeDesc()
                .stream()
                .map(l -> this.modelMapper.map(l, LogViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("logs", logs);

        return this.view("logs", modelAndView);

    }
}
