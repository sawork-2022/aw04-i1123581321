package com.example.webpos.controller;

import com.example.webpos.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PosController {
    private final CartService cartService;

    @Autowired
    PosController(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute
    public void fillData(Model model) {
        model.addAttribute("products", cartService.products());
        model.addAttribute("cart", cartService.content());
        model.addAttribute("tax", cartService.tax());
        model.addAttribute("discount", cartService.discount());
        model.addAttribute("subtotal", cartService.subtotal());
        model.addAttribute("total", cartService.total());
    }

    @GetMapping("/")
    public String pos(Model model, HttpServletRequest request) {
        request.getSession();
        return "index";
    }

    @GetMapping("/add")
    public String addProduct(@RequestParam("id") String id) {
        try {
            cartService.addProduct(id, 1);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/remove")
    public String removeProduct(@RequestParam("id") String id) {
        try {
            cartService.removeProduct(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/sub")
    public String subProduct(@RequestParam("id") String id){
        try {
            cartService.addProduct(id, -1);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/empty")
    public String emptyCart(){
        cartService.resetCart();
        return "redirect:/";
    }
}
