package com.example.cloudfide_igor_barcikowski_task.utils.dynamicfilter;

import com.example.cloudfide_igor_barcikowski_task.product.Product;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;


public class ProductSpecs {

    public static Specification<Product> textSearch(String q) {
        if (q == null || q.isBlank()) return null;

        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("productName")), like),
                cb.like(cb.lower(root.get("description")), like)
        );
    }



    public static Specification<Product> hasProducerName(String producerName) {
        if (producerName == null || producerName.isBlank()) {
            return null;
        }

        String like = "%" + producerName.trim().toLowerCase() + "%";

        return (root, query, cb) ->
                cb.like(cb.lower(root.get("producer").get("name")), like);
    }



    public static Specification<Product> priceGreaterThanOrEqualTo(BigDecimal minPrice) {
        if (minPrice == null) return null;

        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }


    public static Specification<Product> priceLessThanOrEqualTo(BigDecimal maxPrice) {
        if (maxPrice == null) return null;

        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
