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
import org.springframework.web.bind.annotation.RequestMapping;

import vttp5.paf.day24_25ws.model.Order;
import vttp5.paf.day24_25ws.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("web")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // show order form
    @GetMapping("/order")
    public String showOrderForm() 
    {
        // model.addAttribute("order", new Order()); // provide empty order object for the form
        return "orderform";
    }
        
}
