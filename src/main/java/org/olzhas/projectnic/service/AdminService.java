package org.olzhas.projectnic.service;

import org.olzhas.projectnic.dto.CategoryDto;
import org.olzhas.projectnic.entity.projection.UsersAdminProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {

    void deleteUserById(long userId);

    void changeUserStatus(long userId, boolean status);

    Page<UsersAdminProjection> getAllUsers(Pageable pageable);

    Page<UsersAdminProjection> getUsersByIds(Pageable pageable, List<Long> ids);

    void createCategory(CategoryDto category);

    List<CategoryDto> getCategories();

    CategoryDto getCategoryById(long id);

    CategoryDto updateCategory(long id, CategoryDto category);

    void deleteCategoryById(long id);
}
