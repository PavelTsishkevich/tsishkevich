package com.teachmeskills.tsishkevich.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teachmeskills.tsishkevich.model.Product;
import com.teachmeskills.tsishkevich.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private static final long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "first";
    private static final double PRODUCT_PRICE = 2.0;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("check display name with empty list of elements")
    void findAll() throws Exception {
        Mockito.doReturn(new ArrayList<>()).when(productService).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    @DisplayName("check display name with filled list of elements")
    void findAll_with_filled_values() throws Exception {
        Mockito.doReturn(List.of(getMockedProduct())).when(productService).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"id\":1,\"name\":\"first\",\"price\":2.0}]"));
    }

    @Test
    @DisplayName("check findById with exist element")
    void findById_with_exist_element() throws Exception {
        Mockito.doReturn(getMockedProduct()).when(productService)
                .findById(PRODUCT_ID);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + PRODUCT_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":1,\"name\":\"first\",\"price\":2.0}"));
    }

    @Test
    @DisplayName("check create endpoint")
    void create() throws Exception {
        Mockito.doReturn(getMockedProduct()).when(productService)
                .save(Mockito.any(Product.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .content("{\"id\":1,\"name\":\"first\",\"price\":2.0}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":1,\"name\":\"first\",\"price\":2.0}"));
    }

    @Test
    @DisplayName("check update endpoint")
    void save() throws Exception {
        Mockito.doReturn(getMockedProduct()).when(productService)
                .save(Mockito.anyLong(), Mockito.any(Product.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/products/" + PRODUCT_ID)
                        .content("{\"id\":1,\"name\":\"first\",\"price\":2.0}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":1,\"name\":\"first\",\"price\":2.0}"));
    }

    @Test
    @DisplayName("check create endpoint with bad request")
    void create_with_bad_request() throws Exception {
        Mockito.doReturn(getMockedProduct()).when(productService)
                .save(Mockito.any(Product.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("check delete method work")
    void deleteMethod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products"+ "/" + PRODUCT_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Product getMockedProduct() {
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(PRODUCT_NAME);
        product.setPrice(PRODUCT_PRICE);
        return product;
    }
}
