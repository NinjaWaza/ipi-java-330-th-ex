package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Commercial;
import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.model.Manager;
import com.ipiecoles.java.java320.model.Technicien;
import com.ipiecoles.java.java320.service.EmployeService;
import com.ipiecoles.java.java320.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@Controller
@RequestMapping("employes")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @GetMapping(value = "/{id}")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String index(@PathVariable("id") Long id, final ModelMap model) {
        Employe theEmploye = this.employeService.findById(id);
        model.put("employe", theEmploye);
        if(theEmploye instanceof Commercial){
            model.put("commercial", (Commercial)this.employeService.findById(id));
        }
        if(theEmploye instanceof Technicien){
            model.put("technicien", (Technicien)this.employeService.findById(id));
        }
        if(theEmploye instanceof Manager){
            model.put("manager", (Manager)this.employeService.findById(id));
        }
        return "detail";
    }

    @GetMapping(value = "", params = "matricule")
    public String searchEmployeByMatricuel(@RequestParam String matricule,
                                           final ModelMap model){
        model.put("employe",this.employeService.findMyMatricule(matricule));
        return "detail";
    }

    @GetMapping(value="", params = { "page", "size", "sortDirection" , "sortProperty"})
    public String getByMatricule(@RequestParam(name = "page") Integer page,
                                 @RequestParam(name = "size") Integer size,
                                 @RequestParam(name = "sortDirection") String sortDirection,
                                 @RequestParam(name = "sortProperty") String sortProperty,
                                 final ModelMap model){
        Page<Employe> resultPage = this.employeService.findAllEmployes(page,size,sortProperty,sortDirection);
        Long total = this.employeService.countAllEmploye();
        model.put("allEmployes",resultPage);
        model.put("total",total);
        model.put("maxPage",Math.round(total/size));
        model.put("currentPageNumber",page);
        model.put("currentSortDirection",sortDirection);
        return "allEmployes";
    }
}
