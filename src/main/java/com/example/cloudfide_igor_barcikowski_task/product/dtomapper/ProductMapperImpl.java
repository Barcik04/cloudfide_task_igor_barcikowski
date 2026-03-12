package com.example.cloudfide_igor_barcikowski_task.product.dtomapper;

import com.example.cloudfide_igor_barcikowski_task.product.Product;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductResponse;
import com.example.cloudfide_igor_barcikowski_task.productattributes.ProductAttribute;
import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributeResponse;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProductMapperImpl implements  ProductMapper {
    @Override
    public ProductResponse toProductResponseDto(Product product) {
        if (product == null) {
            return null;
        }

        Set<ProductAttributeResponse> productAttributeResponses = new HashSet<>();

        for (ProductAttribute attributeRequest : product.getProductAttributes()) {
            productAttributeResponses.add(
                    new ProductAttributeResponse(
                            attributeRequest.getId(),
                            attributeRequest.getAttributeName(),
                            attributeRequest.getValue()
                    )
            );
        }

        return new ProductResponse(
                product.getId(),
                product.getProducer().getId(),
                product.getProductName(),
                product.getDescription(),
                product.getQuantity(),
                productAttributeResponses,
                product.getPrice()
        );
    }
}
