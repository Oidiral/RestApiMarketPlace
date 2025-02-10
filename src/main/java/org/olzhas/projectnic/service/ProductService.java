package org.olzhas.projectnic.service;

import org.olzhas.projectnic.dto.ProductDto;


public interface ProductService {
    void addProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, Long userId);

    void deleteProduct(Long id);
}
