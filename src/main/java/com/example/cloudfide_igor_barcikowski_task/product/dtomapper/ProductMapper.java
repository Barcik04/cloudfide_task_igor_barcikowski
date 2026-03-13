package com.example.cloudfide_igor_barcikowski_task.product.dtomapper;


import com.example.cloudfide_igor_barcikowski_task.product.Product;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ProductMapper {
    ProductResponse toProductResponseDto(Product product);
    Page<ProductResponse> toProductResponsePageDto(Page<Product> products);
}
