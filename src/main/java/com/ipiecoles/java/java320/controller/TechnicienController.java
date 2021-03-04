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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("techniciens")
public class TechnicienController {

    @Autowired
    private TechnicienService technicienService;

    private EmployeService employeService;

    @GetMapping("/{id}/manager/remove")
    public String removeATheManager(@PathVariable("id") Long id, RedirectAttributes attributes){
        this.technicienService.deleteManager(id);
        attributes.addFlashAttribute("type","success");
        attributes.addFlashAttribute("message","Suppression du manager effectué avec succé");
        return "redirect:/employes/"+ id;
    }


    @GetMapping(value = "/{id}/manager/add", params = "matriculeManagerToAdd")
    public String addAManager(@PathVariable("id") Long id,@RequestParam(name = "matriculeManagerToAdd") String matricule, RedirectAttributes attributes){
        this.technicienService.addManager(id,matricule);
        attributes.addFlashAttribute("type","success");
        attributes.addFlashAttribute("message","Ajout du manager effectué avec succé");
        return "redirect:/employes/"+id;
    }
}
