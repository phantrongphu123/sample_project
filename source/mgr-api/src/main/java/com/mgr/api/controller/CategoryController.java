package com.mgr.api.controller;

import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.ResponseListDto;
import com.mgr.api.dto.category.CategoryDto;
import com.mgr.api.form.category.CreateCategoryForm;
import com.mgr.api.form.category.UpdateCategoryForm;
import com.mgr.api.mapper.CategoryMapper;
import com.mgr.api.model.Category;
import com.mgr.api.model.criteria.CategoryCriteria;
import com.mgr.api.repository.CategoryRepository;
import com.mgr.api.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController extends ABasicController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCategoryForm createCategoryForm, BindingResult bindingResult) {
        categoryService.createCategory(createCategoryForm);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Create category success.");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCategoryForm updateCategoryForm, BindingResult bindingResult) {
        categoryService.updateCategory(updateCategoryForm);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update category success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_V')")
    public ApiMessageDto<CategoryDto> get(@PathVariable("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        ApiMessageDto<CategoryDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(categoryMapper.fromCategoryToDto(category));
        apiMessageDto.setMessage("Get category success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_L')")
    public ApiMessageDto<ResponseListDto<List<CategoryDto>>> list(CategoryCriteria categoryCriteria, Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(
                categoryCriteria.getSpecification(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"))
        );

        ResponseListDto<List<CategoryDto>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(categoryMapper.fromEntityToCategoryDtoList(categoryPage.getContent()));
        responseListDto.setTotalElements(categoryPage.getTotalElements());
        responseListDto.setTotalPages(categoryPage.getTotalPages());

        ApiMessageDto<ResponseListDto<List<CategoryDto>>> apiMessageDto = new ApiMessageDto<>();

        apiMessageDto.setData(responseListDto);

        apiMessageDto.setMessage("List category success.");
        return apiMessageDto;
    }
}