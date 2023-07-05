package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.repository.CategoryRepository;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository theCategoryRepository) {
        this.categoryRepository = theCategoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        Category categorySave = new Category(category.getName());

        return categoryRepository.save(categorySave);
    }

    @Override
    public Category findById(Long id) {
        Optional<Category> result = categoryRepository.findById(id);

        Category category = null;

        if (result.isPresent()) {
            category = result.get();
        }

        return category;
    }


    @Override
    public Category update(Category category) {
        Category categoryUpdate = categoryRepository.findById(category.getId()).get();
        categoryUpdate.setName(category.getName());
        categoryUpdate.setActivated(category.isActivated());
        categoryUpdate.setDeleted(category.isDeleted());

        return categoryRepository.save(categoryUpdate);
    }

    @Override
    public void deleteById(Long id) {
        //categoryRepository.deleteById(id);
        Optional<Category> result = categoryRepository.findById(id);

        Category category = null;

        if (result.isPresent()) {
            category = result.get();
        }

        category.setDeleted(true);
        category.setActivated(false);

        categoryRepository.save(category);

    }

    @Override
    public void enableById(Long id) {
        Optional<Category> result = categoryRepository.findById(id);

        Category category = null;

        if (result.isPresent()) {
            category = result.get();
        }

        category.setDeleted(false);
        category.setActivated(true);

        categoryRepository.save(category);
    }
}
