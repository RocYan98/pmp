package com.debug.pmp.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public abstract class AbstractController {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected void errorLog(Exception e) {
        log.error("异常信息:{}", e.getMessage());
    }
}
