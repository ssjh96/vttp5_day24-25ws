package vttp5.paf.day24_25ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp5.paf.day24_25ws.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("web")
public class OrderController {

    // @Autowired
    // private OrderService orderService;

    @Autowired
    @Qualifier("myredis")
    private RedisTemplate<String, String> template;

    // show order form - post via restcontroller
    @GetMapping("/order")
    public String showOrderForm(Model model) 
    {
        List<String> registeredNames = template.opsForList().range("registrations", 0, -1);
        model.addAttribute("registeredNames", registeredNames); // provide empty order object for the form
        return "orderform";
    }
        
}
