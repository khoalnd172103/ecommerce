package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("size", categories.size());
        model.addAttribute("title", "Category");
        model.addAttribute("categoryNew", new Category());
        return "categories";
    }

    @GetMapping("/updateCategoryForm")
    public String updateCategoryForm(@RequestParam("categoryId") Long categoryId, Model model) {
        Category category = categoryService.findById(categoryId);

        model.addAttribute("category", category);

        return "category-form";
    }

    @GetMapping("/deleteCategory")
    public String deleteCategory(@RequestParam("categoryId") Long categoryId, RedirectAttributes attributes) {
        categoryService.deleteById(categoryId);

        attributes.addFlashAttribute("success", "Deleted successfully");

        return "redirect:/categories";
    }

    @GetMapping("/enableCategory")
    public String enableCategory(@RequestParam("categoryId") Long categoryId, RedirectAttributes attributes) {
        categoryService.enableById(categoryId);

        attributes.addFlashAttribute("success", "Enabled successfully");

        return "redirect:/categories";
    }

    @GetMapping("/update-category")
    public String updateCategory(@ModelAttribute("category") Category category, RedirectAttributes attributes) {

        try {
            categoryService.update(category);
            attributes.addFlashAttribute("success", "Updated successfully");
        } catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("fail", "Duplicate category name");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("fail", "Error server");
        }

        return "redirect:/categories";
    }

    @PostMapping("/add-category")
    public String addCategory(@ModelAttribute("categoryNew") Category categoryNew, RedirectAttributes attributes) {

        try {
            categoryService.save(categoryNew);
            attributes.addFlashAttribute("success", "Added successfully");
        } catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("fail", "Duplicate category name");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("fail", "Error server");
        }

        return "redirect:/categories";
    }


    @GetMapping("/showModal")
    public String showModal(Model model) {
        Category myObject = new Category();
        // Set the properties of the object
        myObject.setName("Value 1");

        model.addAttribute("myObject", myObject);
        return "index";
    }

}
