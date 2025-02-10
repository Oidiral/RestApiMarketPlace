package org.olzhas.projectnic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.olzhas.projectnic.dto.ProductDto;
import org.olzhas.projectnic.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/seller")
@RestController
@RequiredArgsConstructor
public class SellerController {

    private final ProductService productService;

    @PostMapping("/addProduct")
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Valid ProductDto productDto) {
        productService.addProduct(productDto);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @PatchMapping("/updateProduct/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable long id, @RequestBody @Valid ProductDto productDto) {
        productService.updateProduct(productDto, id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct{id}")
    public ResponseEntity<Long> deleteProduct(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
