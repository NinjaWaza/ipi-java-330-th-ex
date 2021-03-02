package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @Autowired
    private EmployeService employeService;

    @GetMapping(value = "/")
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String index(final ModelMap model) {
        model.put("nom", "IPI");
        Long numberOfEmploye = this.employeService.countAllEmploye();
        model.put("nmEmploye", numberOfEmploye);
        return "accueil";
    }
}
