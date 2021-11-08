package com.dbmsproject.foodblog.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

import com.dbmsproject.foodblog.entity.User;
import com.dbmsproject.foodblog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping()
    public String showRegForm(Model model) {
        model.addAttribute("user", new User());
        return "regForm";
    }

    @PostMapping()
    public String processRegistrationForm(
        @Valid @ModelAttribute("user") User user,
        BindingResult bindingResult,
        Model model
    ) {

		logger.info("Processing registration form ");

        String username = user.getUsername();
        		
		 if (bindingResult.hasErrors()){
			 return "regForm";
	        }

        User existing = userService.findByUserName(username);

        if (existing != null) {
            logger.warning("User name already exists.");
            bindingResult.rejectValue("username", "error.user", "There is already a user registered with the username provided");
        }
        
        if (!bindingResult.hasErrors()) {
            //Registration successful, saving user
            userService.save(user);       
            logger.info("Successfully created user: " + username);
            return "home";
        }

        return "regForm";
    }
}
