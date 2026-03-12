package com.example.cloudfide_igor_barcikowski_task.product.dtomapper;


import com.example.cloudfide_igor_barcikowski_task.product.Product;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductResponse;


public interface ProductMapper {
    ProductResponse toProductResponseDto(Product product);
}
