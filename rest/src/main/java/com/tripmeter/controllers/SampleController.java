package com.tripmeter.controllers;

import com.tripmeter.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Project: rest
 * User: Rajiv R. Nair
 * Created: 10/10/13 9:06 PM
 */
@Controller
@RequestMapping("sample")
public class SampleController {

    @RequestMapping("ping")
    @ResponseBody
    public String ping() {
        return "pong";
    }

    @RequestMapping("user/{id}")
    @ResponseBody
    public User getUserById(@PathVariable Long id) {
        return new User(id, "User Number " + id);
    }
}
