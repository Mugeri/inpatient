package org.openmrs.module.inpatient.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.inpatient.Admission;
import org.openmrs.module.inpatient.Inpatient;
import org.openmrs.module.inpatient.Ward;
import org.openmrs.module.inpatient.api.AdmissionService;
import org.openmrs.module.inpatient.api.WardService;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;

/**
 * Created by  banga on 5/27/15.
 */
@Controller
public class InpatientWardController {

    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/module/inpatient/listwards.form", method = RequestMethod.GET)
    public void listWards(ModelMap model) {
        WardService wardService = Context.getService(WardService.class);
        model.addAttribute("ward", wardService.getAllWards());

    }

    @RequestMapping(value = "/module/inpatient/addward.form", method = RequestMethod.GET)
    public void wardForm(ModelMap model) {
        Ward ward=new Ward();
        model.addAttribute("ward",ward);

    }


    @RequestMapping(value = "/module/inpatient/saveWard.form", method=RequestMethod.POST)
    public String saveWardForm(WebRequest request, HttpSession httpSession, ModelMap model,
                               @RequestParam(required = false, value = "action") String action,
                               @ModelAttribute("ward") Ward ward, BindingResult errors)
    {

        WardService wardService = Context.getService(WardService.class);


        if (!Context.isAuthenticated()) {
            errors.reject("ward.auth.required");

        } else
        {

            try {
                wardService.saveWard(ward);
                httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Added Successfully");
                return "redirect:listwards.form";

            }
            catch (Exception ex) {

                httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Addition failed");
            }

        }

        return "redirect:addward.form";
    }


    //method for deleting  ward
    @RequestMapping(value = "/module/inpatient/deleteWard.form", method=RequestMethod.GET)
    public String  deleteWard(ModelMap model, WebRequest webRequest, HttpSession httpSession,
                              @RequestParam(value = "id", required = true) Integer wardId) {
        try {
            WardService wardService=Context.getService(WardService.class);
            Ward ward=wardService.getWard(wardId);
            wardService.purgeWard(ward);
            httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Deleted Successfully");
            return "redirect:listwards.form";
        }
        catch (Exception ex)
        {
            httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Deletion failed");
            return "redirect:listwards.form";

        }

        //return "redirect:manage.form";
    }

    //method to display Edit form for Ward
    @RequestMapping(value = "/module/inpatient/editWard.form", method=RequestMethod.GET)
    public void editWardForm(ModelMap model, WebRequest webRequest, HttpSession httpSession,
                             @RequestParam(value = "id", required = true) Integer wardId) {
        try {
            WardService wardService=Context.getService(WardService.class);
            Ward ward=wardService.getWard(wardId);
            model.addAttribute("ward", ward);

        }
        catch (Exception ex)
        {
            httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Edit form failed");

        }

        //return "redirect:manage.form";
    }

    //Method to save an Ward edit
    @RequestMapping(value = "/module/inpatient/saveWardEdit.form", method=RequestMethod.POST)
    public String saveWardEdit(WebRequest request, HttpSession httpSession, ModelMap model,
                               @RequestParam(required = true, value = "id") Integer wardId,
                               @ModelAttribute("ward") Ward ward, BindingResult errors)
    {

        WardService wardService = Context.getService(WardService.class);



        if (!Context.isAuthenticated()) {
            errors.reject("ward.auth.required");

        } else
        {

            try {

                Ward wardEdit=wardService.getWard(wardId);

                wardEdit.setWardName(ward.getWardName());
                wardEdit.setSpeciality(ward.getSpeciality());
                wardEdit.setDescription(ward.getDescription());
                wardEdit.setCapacity(ward.getCapacity());
                wardEdit.setUuid(ward.getUuid());

                wardService.saveWard(wardEdit);
                httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Edited Ward Successfully");
                return "redirect:listwards.form";

            }
            catch (Exception ex) {

                httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Ward Edit failed");
            }

        }

        return "redirect:listwards.form";
    }


}
