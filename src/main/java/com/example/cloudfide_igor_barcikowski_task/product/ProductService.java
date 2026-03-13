package com.example.cloudfide_igor_barcikowski_task.product;

import com.example.cloudfide_igor_barcikowski_task.exception.ResourceDoesNotBelongException;
import com.example.cloudfide_igor_barcikowski_task.producer.Producer;
import com.example.cloudfide_igor_barcikowski_task.producer.ProducerRepository;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductCreateRequest;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductPatchRequest;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductResponse;
import com.example.cloudfide_igor_barcikowski_task.product.dtomapper.ProductMapper;
import com.example.cloudfide_igor_barcikowski_task.productattributes.ProductAttribute;
import com.example.cloudfide_igor_barcikowski_task.productattributes.ProductAttributeRepository;
import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributeRequest;
import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributesPatchRequest;
import com.example.cloudfide_igor_barcikowski_task.utils.dynamicfilter.ProductFilter;
import com.example.cloudfide_igor_barcikowski_task.utils.dynamicfilter.ProductSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProducerRepository producerRepository;
    private final ProductMapper productMapper;
    private final ProductAttributeRepository  productAttributeRepository;

    public ProductService(ProductRepository productRepository, ProducerRepository producerRepository, ProductMapper productMapper, ProductAttributeRepository productAttributeRepository) {
        this.productRepository = productRepository;
        this.producerRepository = producerRepository;
        this.productMapper = productMapper;
        this.productAttributeRepository = productAttributeRepository;
    }


    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable, ProductFilter productFilter) {
        Pageable safePageable = pageable;

        if (pageable.getPageSize() > 100) {
            safePageable = PageRequest.of(
                    pageable.getPageNumber(),
                    100
            );
        }

        if (productFilter != null
                && productFilter.minPrice() != null
                && productFilter.maxPrice() != null
                && productFilter.minPrice().compareTo(productFilter.maxPrice()) > 0) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
        }

        Specification<Product> spec = Specification
                .where(ProductSpecs.textSearch(productFilter != null ? productFilter.q() : null))
                .and(ProductSpecs.priceGreaterThanOrEqualTo(productFilter != null ? productFilter.minPrice() : null))
                .and(ProductSpecs.priceLessThanOrEqualTo(productFilter != null ? productFilter.maxPrice() : null))
                .and(ProductSpecs.hasProducerName(productFilter != null ? productFilter.producerName() : null));


        Page<Product> products =  productRepository.findAll(spec, safePageable);
        return productMapper.toProductResponsePageDto(products);
    }




    @Transactional
    public ProductResponse createProducts(ProductCreateRequest productCreateRequest, Long producerId) {
        if (productCreateRequest == null) {
            throw new IllegalArgumentException("productCreateRequest cannot be null");
        }

        Producer producer = producerRepository.findById(producerId)
                .orElseThrow(() -> new NoSuchElementException("producer not found"));

        Product product = new Product(
                productCreateRequest.productName(),
                productCreateRequest.description(),
                productCreateRequest.quantity(),
                new HashSet<>(),
                productCreateRequest.price()
        );

        product.setProducer(producer);

        Set<ProductAttribute> attributes = new HashSet<>();

        if (productCreateRequest.productAttributes() != null) {
            for (ProductAttributeRequest attributeRequest : productCreateRequest.productAttributes()) {
                attributes.add(new ProductAttribute(
                        attributeRequest.name(),
                        attributeRequest.value(),
                        product
                ));
            }
        }

        product.setProductAttributes(attributes);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);

    }



    @Transactional
    public ProductResponse patchProduct(ProductPatchRequest patchRequest, Long productId) {
        if (patchRequest == null ||  productId == null) {
            throw new IllegalArgumentException("patchRequest or productId cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("product with this id not found"));

        if (patchRequest.productName() != null && !patchRequest.productName().isBlank()) {product.setProductName(patchRequest.productName());}
        if (patchRequest.productName() != null && !patchRequest.description().isBlank()) {product.setDescription(patchRequest.description());}
        if (patchRequest.quantity() != null) {product.setQuantity(patchRequest.quantity());}
        if (patchRequest.price() != null) {product.setPrice(patchRequest.price());}

        return  productMapper.toProductResponseDto(productRepository.save(product));
    }




    @Transactional
    public ProductResponse patchProductAttribute(ProductAttributesPatchRequest attributesPatchRequest, Long productId, Long attributeId) {
        if (attributesPatchRequest == null || attributeId == null || productId == null) {
            throw new IllegalArgumentException("attributesPatchRequest or attributeId or productId cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("product with this id not found"));

        ProductAttribute attribute = productAttributeRepository.findById(attributeId)
                .orElseThrow(() -> new NoSuchElementException("attribute with this id not found"));

        if (attribute.getProduct() == null || !attribute.getProduct().getId().equals(productId)) {
            throw new ResourceDoesNotBelongException("attribute with id " +  attributeId + " does not belong to product with id " + productId);
        }

        if (attributesPatchRequest.name() != null && !attributesPatchRequest.name().isBlank()) {attribute.setAttributeName(attributesPatchRequest.name());}
        if (attributesPatchRequest.value() != null && !attributesPatchRequest.value().isBlank()) {attribute.setValue(attributesPatchRequest.value());}

        productAttributeRepository.save(attribute);
        return productMapper.toProductResponseDto(product);
    }




    @Transactional
    public void deleteAttribute(Long productId, Long attributeId) {
        if (productId == null || attributeId == null) {
            throw new IllegalArgumentException("productId and attributeId cannot be null");
        }

        productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("product with this id not found"));

        ProductAttribute attribute = productAttributeRepository.findById(attributeId)
                .orElseThrow(() -> new NoSuchElementException("attribute with this id not found"));

        if (attribute.getProduct() == null || !attribute.getProduct().getId().equals(productId)) {
            throw new ResourceDoesNotBelongException("attribute with id " +  attributeId + " does not belong to product with id " + productId);
        }

        productAttributeRepository.delete(attribute);
    }




    @Transactional
    public ProductResponse addAttribute(Long productId, ProductAttributeRequest attributeRequest) {
        if (productId == null || attributeRequest == null) {
            throw new IllegalArgumentException("productId and attributeRequest cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("product with this id not found"));

        ProductAttribute attribute = new ProductAttribute(
                attributeRequest.name(),
                attributeRequest.value()
        );

        product.addProductAttribute(attribute);
        Product savedProduct = productRepository.save(product);
        return  productMapper.toProductResponseDto(savedProduct);
    }




    @Transactional
    public void deleteProduct(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("productId cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("product with this id not found"));

        productRepository.delete(product);
    }



    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("productId cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("product with this id not found"));

        return productMapper.toProductResponseDto(product);
    }
}













