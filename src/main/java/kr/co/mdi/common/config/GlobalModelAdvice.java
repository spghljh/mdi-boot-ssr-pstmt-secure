package kr.co.mdi.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @Autowired
    private DbProfileResolver dbProfileResolver;

    @ModelAttribute("CurrentDbLabel")
    public String currentDbLabel() {
        return "MDI-" + dbProfileResolver.getDbType();
    }
}

