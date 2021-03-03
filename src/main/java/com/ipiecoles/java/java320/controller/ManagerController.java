package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping(value = "/{id}/techniciens/add", params = "matriculeToAdd")
    public String addAnEmployeFromTheTeam(@PathVariable("id") Long id,@RequestParam(name = "matriculeToAdd") String matricule){
        this.managerService.addTechniciens(id,matricule);
        return "redirect:/employes/"+id;
    }

    @GetMapping(value = "/{id}/techniciens/{idTech}/delete")
    public String deleteAnEmployeFromTheTeam(@PathVariable("id") Long id,@PathVariable("idTech") Long idTech){
        this.managerService.deleteTechniciens(id,idTech);
        return "redirect:/employes/"+id;
    }
}
