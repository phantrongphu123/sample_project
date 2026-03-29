package com.mgr.api.mapper;

import com.mgr.api.dto.news.NewsDto;
import com.mgr.api.form.news.CreateNewsForm;
import com.mgr.api.form.news.UpdateNewsForm;
import com.mgr.api.model.News;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NewsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    News fromCreateFormToEntity(CreateNewsForm form);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void mappingUpdateFormToEntity(UpdateNewsForm form, @MappingTarget News news);

    @Mapping(source = "category", target = "category")
    NewsDto fromEntityToDto(News news);

    List<NewsDto> fromEntityListToDtoList(List<News> newsList);
}