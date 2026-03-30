package com.mgr.api.mapper;

import com.mgr.api.dto.category.CategoryDto;
import com.mgr.api.form.category.CreateCategoryForm;
import com.mgr.api.form.category.UpdateCategoryForm;
import com.mgr.api.model.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    /**
     * Map từ Form tạo mới sang Entity Category
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    Category fromCreateCategoryFormToEntity(CreateCategoryForm form);

    /**
     * Cập nhật dữ liệu từ Update Form vào Entity hiện có
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    void mappingUpdateCategoryToEntity(UpdateCategoryForm form, @MappingTarget Category category);

    /**
     * Chuyển đổi từ Entity sang DTO để trả về Client
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCategoryToDto")
    CategoryDto fromCategoryToDto(Category category);

    /**
     * Chuyển đổi danh sách Entity sang danh sách DTO
     */
    @IterableMapping(elementTargetType = CategoryDto.class, qualifiedByName = "fromCategoryToDto")
    @Named("fromEntityToCategoryDtoList")
    List<CategoryDto> fromEntityToCategoryDtoList(List<Category> categories);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCategoryDtoShort")
    CategoryDto fromEntityToCategoryDtoShort(Category category);
}
