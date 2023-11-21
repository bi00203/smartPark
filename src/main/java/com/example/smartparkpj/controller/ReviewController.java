package com.example.smartparkpj.controller;

import com.example.smartparkpj.dto.PageRequestDTO;
import com.example.smartparkpj.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.function.BinaryOperator;

@Controller
@RequestMapping("/review")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviewList")
    public void listGet(PageRequestDTO pageRequestDTO, BindingResult bindingResult, Model model){
        log.info("review List Get !!!!");

        log.info("pageRequestDTO" + reviewService.getList(pageRequestDTO));
        if(bindingResult.hasErrors()){
            pageRequestDTO = PageRequestDTO.builder().build();
        }

        model.addAttribute("responseDTO", reviewService.getList(pageRequestDTO));
    }
}
