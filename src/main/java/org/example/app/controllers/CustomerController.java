package org.example.app.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.domain.entity.customer.Customer;
import org.example.app.service.CustomerService;
import org.example.app.service.DTO.CustomerDTO;
import org.example.app.utils.utils_obj.ResponseData;
import org.example.app.validation.create_update_form.FormDataForValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CustomerController {
    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");

    @Autowired
    private CustomerService customerService;

    @GetMapping("customers")
    public String fetchAllCustomers(Model model) {
        ResponseData<CustomerDTO> allCustomers = customerService.getAll();
        model.addAttribute("listCustomers", allCustomers.getData());
        model.addAttribute("fragmentName", "customers_list");
        return "index";
    }

    @GetMapping("customers/create")
    public String createCustomer(Model model) {
        model.addAttribute("fragmentName", "customer_add");
        return "index";
    }

    @PostMapping("customers/create")
    public RedirectView addCustomer(@ModelAttribute FormDataForValidate input, RedirectAttributes attributes) {
        ResponseData<Customer> createResponse = customerService.create(input);
        CONSOLE_LOGGER.info(input.getLastName());
        if (createResponse.isSuccess()) {
            RedirectView successRedirectView = new RedirectView("/customers");
            attributes.addFlashAttribute("successMsg", createResponse.getMsg());
            CONSOLE_LOGGER.info("Customer created successfully.");
            return successRedirectView;
        } else {
            RedirectView errorRedirectView = new RedirectView("/customers/create");
            attributes.addFlashAttribute("errors", createResponse.getCommonErrors());
            attributes.addFlashAttribute("formData", input);
            attributes.addFlashAttribute("validationFormErrors", createResponse.getValidationFormErrors());
            return errorRedirectView;
        }

    }

    @GetMapping("customers/update")
    public String updateCustomer(Model model, @RequestParam("id") Long id) {
        ResponseData<Customer> getById = customerService.getById(id);
        if (getById.isSuccess()){
            model.addAttribute("fragmentName", "customer_update");
            model.addAttribute("customer", getById.getData().getFirst());
            return "index";
        }
        ResponseData<CustomerDTO> allCustomers = customerService.getAll();
        model.addAttribute("listCustomers", allCustomers.getData());
        model.addAttribute("fragmentName", "customers_list");
        model.addAttribute("errorMsg", getById.getMsg());
        return "index";
    }

    @PostMapping("customers/update/processing")
    public RedirectView updateCustomer(@ModelAttribute FormDataForValidate input, RedirectAttributes attributes) {
        ResponseData<Customer> updateResponse = customerService.update(input);
        if (updateResponse.isSuccess()) {
            RedirectView successRedirectView = new RedirectView("/customers");
            attributes.addFlashAttribute("successMsg", updateResponse.getMsg());
            CONSOLE_LOGGER.info("Customer update successfully.");
            return successRedirectView;
        } else {
            attributes.addFlashAttribute("errors", updateResponse.getCommonErrors());
            attributes.addFlashAttribute("formData", input);
            attributes.addFlashAttribute("validationFormErrors", updateResponse.getValidationFormErrors());
            return new RedirectView("/customers/update?id=" + input.getId());
        }

    }

    @PostMapping("customers/delete")
    public RedirectView deleteCustomer(@ModelAttribute("id") Long id, RedirectAttributes attributes) {
        ResponseData<Customer> deleteResponse = customerService.delete(id);
        if (deleteResponse.isSuccess()) {
            RedirectView successRedirectView = new RedirectView("/customers");
            attributes.addFlashAttribute("successMsg", deleteResponse.getMsg());
            CONSOLE_LOGGER.info("Customer deleted successfully.");
            return successRedirectView;
        } else {
            RedirectView errorRedirectView = new RedirectView("/customers");
            attributes.addFlashAttribute("errors", deleteResponse.getCommonErrors());
            return errorRedirectView;
        }
    }

}
