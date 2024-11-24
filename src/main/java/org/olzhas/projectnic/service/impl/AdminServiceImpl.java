package org.olzhas.projectnic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olzhas.projectnic.dto.CategoryDto;
import org.olzhas.projectnic.entity.Category;
import org.olzhas.projectnic.entity.User;
import org.olzhas.projectnic.entity.projection.UsersAdminProjection;
import org.olzhas.projectnic.exception.ChangeStatusException;
import org.olzhas.projectnic.exception.NotFoundException;
import org.olzhas.projectnic.mapper.CategoryMapper;
import org.olzhas.projectnic.repository.CategoryRepository;
import org.olzhas.projectnic.repository.UserRepository;
import org.olzhas.projectnic.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void changeUserStatus(long userId, boolean status) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with user " + userId + " not found"));
        log.info("Attempting to change status for user with user {}. Current status: {}, New status: {}", userId, user.isActive(), status);
        if (user.isActive() != status) {
            user.setActive(status);
            userRepository.save(user);
            log.info("Status for user with user {} successfully changed to {}", userId, status);
        } else {
            log.warn("Failed to change status for user with user {}. User already in desired status: {}", userId, status);
            throw new ChangeStatusException("User with user " + userId + " is already in the desired status");
        }
    }

    @Override
    public Page<UsersAdminProjection> getAllUsers(Pageable pageable) {
        log.info("Fetching all user for admin view.");
        return userRepository.findAllUsers(pageable);
    }


    @Override
    public Page<UsersAdminProjection> getUsersByIds(Pageable pageable, List<Long> ids) {
        Page<UsersAdminProjection> users = userRepository.findAllByIdIn(pageable, ids);
        log.info("Fetched {} user by given IDs", users.getTotalElements());
        return users;
    }

    @Override
    @Transactional
    public void createCategory(CategoryDto category) {
        Category categoryEntity = categoryMapper.toEntity(category);
        categoryRepository.save(categoryEntity);
        log.info("Created new category {}", categoryEntity.getName());
        categoryMapper.toDto(categoryEntity);
    }

    @Override
    public List<CategoryDto> getCategories() {
        log.info("Fetching all categories for admin view.");
        return categoryMapper.toDto(categoryRepository.findAll());
    }

    @Override
    public CategoryDto getCategoryById(long id) {
        log.info("Fetching category by id {}", id);
        return categoryRepository.findById(id)
                .map(category -> {
                    log.info("Found category {}", category.getName());
                    return categoryMapper.toDto(category);
                })
                .orElseThrow(() -> {
                    log.warn("Failed to fetch category by id {}", id);
                    return new NotFoundException("Category with id " + id + " not found");
                });
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(long id, CategoryDto category) {
        Category categoryEntity = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with id " + id + " not found")
        );
        categoryMapper.partialUpdate(category, categoryEntity);
        categoryRepository.save(categoryEntity);
        log.info("Updated new category {}", categoryEntity.getName());
        return categoryMapper.toDto(categoryEntity);
    }

    @Override
    @Transactional
    public void deleteCategoryById(long id) {
        log.info("Deleting category by id {}", id);
        Category categoryEntity = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with id " + id + " not found")
        );
        categoryRepository.delete(categoryEntity);
    }


    @Override
    @Transactional
    public void deleteUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User with user " + userId + " not found"));
        userRepository.delete(user);
        log.info("User with user {} successfully deleted", userId);
    }

}
