package com.debug.pmp.common.utils;

import org.springframework.validation.BindingResult;

public class ValidatorUtil {

    public static String checkResult(BindingResult bindingResult) {

        if (bindingResult != null && bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            bindingResult.getAllErrors().stream().forEach(error -> sb.append(error.getDefaultMessage()).append("<br>"));
            return sb.toString();
        } else {
            return "";
        }

    }
}
