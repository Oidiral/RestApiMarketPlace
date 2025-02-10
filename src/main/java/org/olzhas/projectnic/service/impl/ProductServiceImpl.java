package org.olzhas.projectnic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olzhas.projectnic.dto.ProductDto;
import org.olzhas.projectnic.entity.Category;
import org.olzhas.projectnic.entity.Product;
import org.olzhas.projectnic.entity.User;
import org.olzhas.projectnic.exception.NotFoundException;
import org.olzhas.projectnic.mapper.ProductMapper;
import org.olzhas.projectnic.repository.CategoryRepository;
import org.olzhas.projectnic.repository.ProductsRepository;
import org.olzhas.projectnic.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public void addProduct(ProductDto productDto) {

        log.info("Attempting to add product for user {} in category {}", productDto.getUserId(), productDto.getCategoryId());

        Category category = findCategoryById(productDto);

        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .description(productDto.getDescription())
                .category(category)
                .isActive(productDto.isActive())
                .user(User.builder().id(productDto.getUserId()).build())
                .build();
        productsRepository.save(product);
        log.info("Product added successfully for user {}", productDto.getUserId());
    }


    @Override
    @Transactional
    @PreAuthorize("authentication.principal.id == #userId or hasRole('ROLE_ADMIN')")
    public ProductDto updateProduct(ProductDto productDto, Long userId) {
        log.info("Attempting to update product for user {} in category {}", productDto.getUserId(), productDto.getCategoryId());
        Product product;
        product = productsRepository.findByUserIdAndProductId(userId, productDto.getId()).orElseThrow(
                () -> new NotFoundException("Not found product with id " + productDto.getId())
        );
        productMapper.partialUpdate(productDto, product);
        productsRepository.save(product);
        log.info("Product updated successfully for user {}", productDto.getUserId());
        return productMapper.toDto(product);
    }


    @Transactional
    @Override
    @PreAuthorize("authentication.principal.id == #userId or hasRole('ROLE_ADMIN')")
    public void deleteProduct(Long id) {
        Product product = findByProductId(id);
        int rowsAffected = productsRepository.deleteByProductIdAndSellerId(product.getId(), product.getUser().getId());
        if (rowsAffected == 0) {
            throw new NotFoundException("Product not found or access denied");
        }
        log.info("Product deleted successfully for user {}", product.getUser().getId());
    }


    private Category findCategoryById(ProductDto productDto) {
        return categoryRepository.findById(productDto.getCategoryId()).orElseThrow(()
                -> new NotFoundException("Category Not Found"));
    }

    private Product findByProductId(Long id) {
        return productsRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Product not found"));
    }

}
