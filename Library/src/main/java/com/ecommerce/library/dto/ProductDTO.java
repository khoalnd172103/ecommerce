package com.ecommerce.library.dto;

import com.ecommerce.library.model.Category;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    private  String name;

    private String description;

    private double costPrice;

    private double salePrice;

    private int currentQuantity;

    private Category category;

    private String image;

    private boolean isActivated;

    private boolean isDeleted;

    public String getPhotosImagePath() {
        if (image == null || id == null) return null;

        return "/image-product/" + id + "/" + image;
    }
}
