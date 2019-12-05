package com.debug.pmp.server.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SysPageController {

    @GetMapping("/login.html")
    public String login() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/index.html";
        }
        return "login";
    }

    @GetMapping({"/index.html", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/main.html")
    public String main() {
        return "main";
    }

    @GetMapping("/404.html")
    public String notFound() {
        return "404";
    }

    @GetMapping("/modules/{module}/{page}.html")
    public String page(@PathVariable String module, @PathVariable String page) {
        return "modules/" + module + "/" + page;
    }


}