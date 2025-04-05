package projeto.hugo.terapia.cloudfare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.cloudfare.service.CloudfareService;

@RestController
@RequestMapping("/cloudfare")
@RequiredArgsConstructor
public class CloudfareController {

    private final CloudfareService cloudfareService;

}
