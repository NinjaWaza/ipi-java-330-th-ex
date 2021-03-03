package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Commercial;
import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.model.Manager;
import com.ipiecoles.java.java320.model.Technicien;
import com.ipiecoles.java.java320.service.EmployeService;
import com.ipiecoles.java.java320.service.ManagerService;
import com.ipiecoles.java.java320.service.TechnicienService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

@Controller
@RequestMapping("employes")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private TechnicienService technicienService;

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

    //On peut ajouter , params = { "page", "size", "sortDirection" , "sortProperty"} dans @GetMapping mais du coup on n'utilisera jamais les valeurs pas défaut
    // car le seul moyen d'afficher cette route sera d'utilisé les 4 paramètres.
    @GetMapping(value="")
    public String getByMatricule(@RequestParam(name = "page",defaultValue = "0") Integer page,
                                 @RequestParam(name = "size",defaultValue = "10") Integer size,
                                 @RequestParam(name = "sortDirection", defaultValue = "ASC") String sortDirection,
                                 @RequestParam(name = "sortProperty", defaultValue = "matricule") String sortProperty,
                                 final ModelMap model){
        Page<Employe> resultPage = this.employeService.findAllEmployes(page,size,sortProperty,sortDirection);
        Long total = this.employeService.countAllEmploye();
        model.put("allEmployes",resultPage);
        model.put("total",total);
        model.put("maxPage",Math.round(total/size));
        model.put("currentPageNumber",page);
        model.put("currentSortDirection",sortDirection);
        model.put( "start" , page * size + 1 ) ;
        model.put("inverseSortDirection", sortDirection.equals("ASC") ? "DESC":"ASC");
        return "allEmployes";
    }

    @GetMapping("/new/{typeEmploye}")
    public String newEmploye(@PathVariable String typeEmploye, final ModelMap model){
        Employe employe = null;
        switch (typeEmploye.toLowerCase()){
            case "technicien": employe = new Technicien(); break;
            case "commercial": employe = new Commercial(); break;
            case "manager": employe = new Manager(); break;
            default: throw new IllegalArgumentException("Type d'employé incorrect !");
        }
        model.put("employe", employe);
        return "detail";
    }

    @PostMapping(value = "/technicien", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createTechnicien(Technicien employe, final ModelMap model){
        return saveEmploye(employe,model);
    }

    @PostMapping(value = "/commercial", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createCommercial(Commercial employe, final ModelMap model){
        return saveEmploye(employe,model);
    }

    @PostMapping(value = "/manager", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createManager(Manager employe, final ModelMap model){
        return saveEmploye(employe,model);
    }

    @PostMapping(value = "/commercial/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateCommercial(Commercial employe, final ModelMap model){
        return updateEmploye(employe,model);
    }

    @PostMapping(value = "/technicien/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateTechnicien(Technicien employe, final ModelMap model){
        return updateEmploye(employe,model);
    }

    @PostMapping(value = "/manager/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateManager(@PathVariable("id") Long id,Manager employe, final ModelMap model){
        return updateEmploye(employe,model);
    }

    @GetMapping(value = "/{id}/delete")
    public String deleteEmploye(@PathVariable("id") Long id){
        this.employeService.deleteEmploye(id);
        return "redirect:/employes";
    }

    private String saveEmploye(Employe employe, ModelMap model){
        employe = employeService.creerEmploye(employe);
        model.put("employe", employe);
        return "redirect:/employes/" + employe.getId();
    }

    private String updateEmploye(Employe employe, ModelMap model){
        employe = employeService.updateEmploye(employe.getId(),employe);
        model.put("employe", employe);
        return "redirect:/employes/" + employe.getId();
    }
}
