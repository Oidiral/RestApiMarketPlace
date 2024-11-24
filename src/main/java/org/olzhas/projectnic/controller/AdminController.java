package org.olzhas.projectnic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.olzhas.projectnic.dto.CategoryDto;
import org.olzhas.projectnic.entity.projection.UsersAdminProjection;
import org.olzhas.projectnic.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/adminBan/{id}")
    public ResponseEntity<String> banUser(@PathVariable("id") long id) {
        adminService.changeUserStatus(id, true);
        return new ResponseEntity<>("Banned", HttpStatus.OK);
    }


    @PostMapping("/deleteUser/{id}")
    public ResponseEntity<Long> deleteUser(@PathVariable("id") long id) {
        adminService.deleteUserById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/unbanUser/{id}")
    public ResponseEntity<Long> unbanUser(@PathVariable("id") long id) {
        adminService.changeUserStatus(id, true);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/create/category")
    public ResponseEntity<Long> createCategory(@RequestBody @Valid CategoryDto category) {
        adminService.createCategory(category);
        return new ResponseEntity<>(category.getId(), HttpStatus.OK);
    }

    @GetMapping("/getAll/category")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        return new ResponseEntity<>(adminService.getCategories(), HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") long id) {
        return new ResponseEntity<>(adminService.getCategoryById(id), HttpStatus.OK);
    }

    @PatchMapping("/categoryUpdate/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") long id, @RequestBody @Valid CategoryDto category) {
        return new ResponseEntity<>(adminService.updateCategory(id, category), HttpStatus.OK);
    }

    @DeleteMapping("/categoryDelete/{id}")
    public ResponseEntity<Long> deleteCategory(@PathVariable("id") long id) {
        adminService.deleteCategoryById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/getAll/users")
    public ResponseEntity<Page<UsersAdminProjection>> getAllUsers(@RequestParam() @PageableDefault(size = 5) Pageable pageable) {
        Page<UsersAdminProjection> usersPage = adminService.getAllUsers(pageable);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }

    @GetMapping("/by-ids")
    public Page<UsersAdminProjection> getUsersByIds(@RequestParam List<Long> ids,
                                                    @PageableDefault(size = 5) Pageable pageable) {
        return adminService.getUsersByIds(pageable, ids);
    }

}
