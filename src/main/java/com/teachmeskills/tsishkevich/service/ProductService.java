package com.teachmeskills.tsishkevich.service;

import com.teachmeskills.tsishkevich.model.Product;
import com.teachmeskills.tsishkevich.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product hasn't found"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product save(Long id, Product product) {
        if (!id.equals(product.getId())) {
            product.setId(id);
        }
        System.out.println("my new commit");
        return save(product);
    }

    public void deleteById(long id) {
        productRepository.deleteById(id);
    }
}
