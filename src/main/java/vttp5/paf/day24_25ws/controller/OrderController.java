package vttp5.paf.day24_25ws.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp5.paf.day24_25ws.model.Order;
import vttp5.paf.day24_25ws.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;



@Controller
@RequestMapping("web")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // show order form - post via restcontroller
    @GetMapping("/order")
    public String showOrderForm() 
    {
        // model.addAttribute("order", new Order()); // provide empty order object for the form
        return "orderform";
    }

    // // show order form - post via controller
    // @GetMapping("/order2") 
    // public String showOrderForm2(Model model) 
    // {
    //     model.addAttribute("order", new Order()); // provide empty order object for the form
    //     return "orderform2";
    // }

    // @PostMapping("/order2")
    // public String postOrderForm2(@Valid @ModelAttribute ("order") Order order, BindingResult bindingResult, Model model) {
        
    //     // if (bindingResult.hasErrors()) {
    //     //     return "orderform2"; // display form again with errors
    //     // }

    //     boolean isCreated = orderService.createOrder(order);

    //     if (!isCreated)
    //     {
    //         model.addAttribute("errormsg", "Order failed to create..");
    //         return "errorpage";
    //     }

    //     model.addAttribute("success", "Order created succesfully!");
    //     return "success";
    // }
        
}
