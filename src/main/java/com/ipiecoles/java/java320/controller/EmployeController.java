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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
            model.put("title", "Détail Commercial "+theEmploye.getMatricule());
        }
        if(theEmploye instanceof Technicien){
            model.put("technicien", (Technicien)this.employeService.findById(id));
            model.put("title", "Détail Technicien "+theEmploye.getMatricule());
        }
        if(theEmploye instanceof Manager){
            model.put("manager", (Manager)this.employeService.findById(id));
            model.put("title", "Détail Manager "+theEmploye.getMatricule());
        }
        return "detail";
    }

    @GetMapping(value = "", params = "matricule")
    public String searchEmployeByMatricuel(@RequestParam String matricule,
                                           final ModelMap model){
        model.put("employe",this.employeService.findMyMatricule(matricule));
        model.put("title", "Détail employé ");
        return "detail";
    }

    //On peut ajouter , params = { "page", "size", "sortDirection" , "sortProperty"} dans @GetMapping mais du coup on n'utilisera jamais les valeurs pas défaut
    // car le seul moyen d'afficher cette route sera d'utilisé les 4 paramètres.
    @GetMapping(value="")
    public String allEmployees(@RequestParam(name = "page",defaultValue = "0") Integer page,
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
        model.put("title", "Liste de tous les employés");
        model.put("sizes", Arrays.asList("5","10","20","50"));
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
        model.put("title", "Détail employé");
        return "detail";
    }

    @PostMapping(value = "/technicien", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createTechnicien(Technicien employe, final ModelMap model, RedirectAttributes attributes){
        return saveEmploye(employe,model, attributes);
    }

    @PostMapping(value = "/commercial", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createCommercial(Commercial employe, final ModelMap model, RedirectAttributes attributes){
        return saveEmploye(employe,model, attributes);
    }

    @PostMapping(value = "/manager", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createManager(Manager employe, final ModelMap model, RedirectAttributes attributes) throws Exception {
        if(employe.getDateEmbauche() == null){
            employe.setDateEmbauche(LocalDate.now());
        }
        return saveEmploye(employe,model, attributes);
    }

    @PostMapping(value = "/commercial/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateCommercial(Commercial employe, final ModelMap model, RedirectAttributes attributes){
        return updateEmploye(employe,model, attributes);
    }

    @PostMapping(value = "/technicien/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateTechnicien(Technicien employe, final ModelMap model, RedirectAttributes attributes){
        return updateEmploye(employe,model, attributes);
    }

    @PostMapping(value = "/manager/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateManager(@PathVariable("id") Long id,Manager employe, final ModelMap model, RedirectAttributes attributes) throws Exception {
        if(employe.getDateEmbauche() == null){
            employe.setDateEmbauche(LocalDate.now());
        }
        if(employe.getSalaire() == null){
            employe.setSalaire(1480.27d);
        }
        if(employe.getSalaire()!=null){
            employe.setSalaire(employe.getSalaire());
        }
        return updateEmploye(employe,model, attributes);
    }

    @GetMapping(value = "/{id}/delete")
    public String deleteEmploye(@PathVariable("id") Long id, RedirectAttributes attributes){

        try{
            this.employeService.deleteEmploye(id);
        }
        catch(Exception e)
        {
            attributes.addFlashAttribute("type","danger");
            attributes.addFlashAttribute("message","Erreur lors de la sauvegarde de l'employé !");
        }
        return "redirect:/employes";
    }

    private String saveEmploye(Employe employe, ModelMap model, RedirectAttributes attributes){
        try{
            employe = employeService.creerEmploye(employe);
            attributes.addFlashAttribute("message", "Ajout de l'employé effectuée");
            model.put("employe", employe);
            attributes.addFlashAttribute("type", "success");
        }
        catch(Exception e)
        {
         attributes.addFlashAttribute("type","danger");
         attributes.addFlashAttribute("message","Erreur lors de la sauvegarde de l'employé !");
        }
        return "redirect:/employes/" + employe.getId();
    }

    private String updateEmploye(Employe employe, ModelMap model, RedirectAttributes attributes){
        try{
            employe = employeService.updateEmploye(employe.getId(),employe);
            attributes.addFlashAttribute("message", "Modification de l'employé effectuée");
            model.put("employe", employe);
            attributes.addFlashAttribute("type", "success");
        }
        catch(Exception e)
        {
            attributes.addFlashAttribute("type","danger");
            attributes.addFlashAttribute("message","Erreur lors de la mise à jour de l'employé !");
        }
        return "redirect:/employes/" + employe.getId();
    }
}
