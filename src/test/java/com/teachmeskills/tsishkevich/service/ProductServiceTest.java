package com.teachmeskills.tsishkevich.service;

import com.teachmeskills.tsishkevich.model.Product;
import com.teachmeskills.tsishkevich.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private static final long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "first";
    private static final double PRODUCT_PRICE = 2.0;
    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        productRepository = mock(ProductRepository.class);
        productService = spy(new ProductService(productRepository));
    }

    @Test
    @DisplayName("check findAll method")
    void findAll() {
        Product product =getMockedProduct();
        doReturn(List.of(product)).when(productRepository).findAll();
        List<Product> products = productService.findAll();
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    @DisplayName("check if error throws when Product is not found")
    void findById_throws_exception() {
        doThrow(new RuntimeException("Product hasn't found"))
                .when(productRepository).findById(PRODUCT_ID);

        Exception exception =
                assertThrows(RuntimeException.class, () -> productService.findById(PRODUCT_ID));

        String expectedMessage = "Product hasn't found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("check if Product returns when it is found")
    void findById_returns_product() {
        Product mockedProduct = getMockedProduct();
        doReturn(Optional.of(mockedProduct))
                .when(productRepository).findById(PRODUCT_ID);

        Product product = productService.findById(PRODUCT_ID);

        assertEquals(mockedProduct, product);
    }

    @Test
    void deleteById() {
        doNothing().when(productRepository).deleteById(PRODUCT_ID);
        productService.deleteById(PRODUCT_ID);

        verify(productRepository, times(1)).deleteById(PRODUCT_ID);
        verify(productRepository, times(1)).deleteById(anyLong());
        verify(productRepository, never()).findById(anyLong());
    }

    @Test
    void save_with_same_id() {
        Product mockedProduct = getMockedProduct();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(productRepository)
                .save(any(Product.class));

        Product result = productService.save(PRODUCT_ID, mockedProduct);
        assertEquals(mockedProduct, result);
        assertEquals(PRODUCT_ID, result.getId());
    }

    @Test
    void save_with_different_id() {
        Product mockedProduct = getMockedProduct();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(productRepository)
                .save(any(Product.class));

        Product result = productService.save(PRODUCT_ID + 1, mockedProduct);
        assertEquals(mockedProduct, result);
        assertEquals(PRODUCT_ID + 1, result.getId());
    }



    private Product getMockedProduct() {
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(PRODUCT_NAME);
        product.setPrice(PRODUCT_PRICE);
        return product;
    }
}
