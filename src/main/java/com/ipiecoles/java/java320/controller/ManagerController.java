package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping(value = "/{id}/techniciens/add", params = "matriculeToAdd")
    public String addAnEmployeFromTheTeam(@PathVariable("id") Long id,@RequestParam(name = "matriculeToAdd") String matricule, RedirectAttributes attributes){
        this.managerService.addTechniciens(id,matricule);
        attributes.addFlashAttribute("type","success");
        attributes.addFlashAttribute("message","Suppression du technicien de l'équipe effectué");
        return "redirect:/employes/"+id;
    }

    @GetMapping(value = "/{id}/techniciens/{idTech}/delete")
    public String deleteAnEmployeFromTheTeam(@PathVariable("id") Long id, @PathVariable("idTech") Long idTech, RedirectAttributes attributes){
        this.managerService.deleteTechniciens(id,idTech);
        attributes.addFlashAttribute("type","success");
        attributes.addFlashAttribute("message","Suppression du technicien de l'équipe effectué");
        return "redirect:/employes/"+id;
    }
}
