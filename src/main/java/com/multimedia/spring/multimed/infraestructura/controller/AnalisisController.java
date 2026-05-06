package com.multimedia.spring.multimed.infraestructura.controller;

import com.multimedia.spring.multimed.aplicacion.dto.AprioriRuleDTO;
import com.multimedia.spring.multimed.aplicacion.service.AnalisisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analisis")
public class AnalisisController {

    private final AnalisisService analisisService;

    public AnalisisController(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    @GetMapping("/apriori")
    public List<AprioriRuleDTO> getAprioriRules(
            @RequestParam(defaultValue = "0.15") double support,
            @RequestParam(defaultValue = "0.7") double confidence) {
        return analisisService.generarReglasApriori(support, confidence);
    }
}
