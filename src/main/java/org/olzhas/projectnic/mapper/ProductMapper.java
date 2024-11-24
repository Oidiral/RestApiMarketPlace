package org.olzhas.projectnic.mapper;

import org.mapstruct.*;
import org.olzhas.projectnic.dto.ProductDto;
import org.olzhas.projectnic.entity.Category;
import org.olzhas.projectnic.entity.Product;
import org.olzhas.projectnic.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "userId", target = "user")
    Product toEntity(ProductDto productDto);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "user.id", target = "userId")
    ProductDto toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "userId", target = "user")
    Product partialUpdate(ProductDto productDto, @MappingTarget Product product);

    default Category createCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    default User createUsers(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
