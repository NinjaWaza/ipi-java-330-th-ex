package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Technicien;
import com.ipiecoles.java.java320.service.EmployeService;
import com.ipiecoles.java.java320.service.TechnicienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("techniciens")
public class TechnicienController {

    @Autowired
    private TechnicienService technicienService;

    private EmployeService employeService;

    @GetMapping("/{id}/manager/remove")
    public String removeATheManager(@PathVariable("id") Long id){
        this.technicienService.deleteManager(id);
        return "redirect:/employes/"+ id;
    }


    @GetMapping(value = "/{id}/manager/add", params = "matriculeManagerToAdd")
    public String addAManager(@PathVariable("id") Long id,@RequestParam(name = "matriculeManagerToAdd") String matricule){
        this.technicienService.addManager(id,matricule);
        return "redirect:/employes/"+id;
    }
}
