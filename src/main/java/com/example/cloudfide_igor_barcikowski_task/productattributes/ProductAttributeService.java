package com.example.cloudfide_igor_barcikowski_task.productattributes;


import com.example.cloudfide_igor_barcikowski_task.product.ProductRepository;

import com.example.cloudfide_igor_barcikowski_task.product.dtomapper.ProductMapper;
import org.springframework.stereotype.Service;



@Service
public class ProductAttributeService {
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductAttributeService(ProductAttributeRepository productAttributeRepository, ProductMapper productMapper, ProductRepository productRepository) {
        this.productAttributeRepository = productAttributeRepository;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }


}
