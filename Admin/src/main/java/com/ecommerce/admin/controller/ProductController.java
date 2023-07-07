package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDTO;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService theProductService, CategoryService theCategoryService) {
        this.productService = theProductService;
        this.categoryService = theCategoryService;
    }

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        List<ProductDTO> productDTOList = productService.findAll();
        model.addAttribute("products", productDTOList);
        model.addAttribute("size", productDTOList.size());
        model.addAttribute("title", "Product");
        return "products";
    }

    @GetMapping("/productAddForm")
    public String productAddForm(Model model) {
        Product product = new Product();
        List<Category> categories = categoryService.findAllByIsActivated();

        model.addAttribute("categories", categories);
        model.addAttribute("title", "Add product");
        model.addAttribute("product", product);

        return "product-add-form";
    }

    @GetMapping("/updateProductForm")
    public String updateProductForm(@RequestParam("productId") Long id, Model model) {
        Product product = productService.findById(id);
        List<Category> categories = categoryService.findAllByIsActivated();

        model.addAttribute("categories", categories);
        model.addAttribute("title", "Update product");
        model.addAttribute("product", product);

        return "product-update-form";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("productNew") Product product,
                             @RequestParam("file") MultipartFile multipartFile,
                             RedirectAttributes attributes) {
        try {
            String fileName = ImageUpload.handleFileUpload(multipartFile);
            product.setImage(fileName);
            Product saveProduct = productService.save(product);

            String uploadDir = "image-product/" + saveProduct.getId();

            ImageUpload.uploadImage(uploadDir, multipartFile);

            attributes.addFlashAttribute("success", "Added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("fail", "Error server");
        }

        return "redirect:/products";
    }

    @PostMapping("/update-product")
    public String updateProduct(
            @RequestParam("file") MultipartFile multipartFile,
            @ModelAttribute("product") Product product
            //@RequestParam("productId") Long productId
    ) throws IOException {

        Product saveProduct = productService.update(multipartFile, product);

        System.out.println(saveProduct);

        return "redirect:/products";
    }

    @GetMapping("/deleteProduct")
    public String deleletProduct(@RequestParam("productId") Long id, RedirectAttributes attributes) {
        productService.deleteById(id);

        attributes.addFlashAttribute("success", "Deleted successfully");

        return "redirect:/products";
    }

    @GetMapping("/enableProduct")
    public String enableProduct(@RequestParam("productId") Long id, RedirectAttributes attributes) {
        productService.enableById(id);

        attributes.addFlashAttribute("success", "Enabled successfully");

        return "redirect:/products";
    }

}
