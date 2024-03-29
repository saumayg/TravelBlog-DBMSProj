package com.dbmsproject.travelblog.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.UserService;

///Controller for registration related services
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

    ///Show registration page
    @GetMapping()
    public String showRegForm(Model model) {
        logger.info("RegistrationController: Show registration page");

        model.addAttribute("user", new User());
        return "regForm";
    }

    ///Registration handler
    @PostMapping()
    public String processRegistrationForm(
        @Valid @ModelAttribute("user") User user,
        BindingResult bindingResult,
        @RequestParam(value = "profile", required = false) MultipartFile multipartFile,
        Model model
    ) throws IOException {
		logger.info("RegistrationController: Processing registration form ");

        //Get username
        String username = user.getUsername();
        		
		 if (bindingResult.hasErrors()){
			 return "regForm";
	    }

        //If user with username exists
        User existing = userService.findByUserName(username);
        if (existing != null) {
            logger.warning("User name already exists.");
            bindingResult.rejectValue("username", "error.user", "There is already a user registered with the username provided");
        }
        
        if (!bindingResult.hasErrors()) {
            //Registration successful, saving user
            userService.save(user, false, multipartFile);
            return "regConfirm";
        }

        return "regForm";
    }
}
