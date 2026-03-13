package com.example.cloudfide_igor_barcikowski_task.product.dtomapper;

import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerSimpleInfo;
import com.example.cloudfide_igor_barcikowski_task.product.Product;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductResponse;
import com.example.cloudfide_igor_barcikowski_task.productattributes.ProductAttribute;
import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ProductMapperImpl implements  ProductMapper {
    @Override
    public ProductResponse toProductResponseDto(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product cannot be null");
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

        ProducerSimpleInfo producerSimpleInfo = new ProducerSimpleInfo(
                product.getProducer().getId(),
                product.getProducer().getName(),
                product.getProducer().getCountry()
        );

        return new ProductResponse(
                product.getId(),
                producerSimpleInfo,
                product.getProductName(),
                product.getDescription(),
                product.getQuantity(),
                productAttributeResponses,
                product.getPrice()
        );
    }




    @Override
    public Page<ProductResponse> toProductResponsePageDto(Page<Product> products) {
        if  (products == null) {
            throw new IllegalArgumentException("products cannot be null");

        }

        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
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

            ProducerSimpleInfo producerSimpleInfo = new ProducerSimpleInfo(
                    product.getProducer().getId(),
                    product.getProducer().getName(),
                    product.getProducer().getCountry()
            );


            productResponses.add(
                    new ProductResponse(
                            product.getId(),
                            producerSimpleInfo,
                            product.getProductName(),
                            product.getDescription(),
                            product.getQuantity(),
                            productAttributeResponses,
                            product.getPrice()
                    )
            );
        }

        return new PageImpl<>(productResponses, products.getPageable(), products.getTotalElements());
    }
}
