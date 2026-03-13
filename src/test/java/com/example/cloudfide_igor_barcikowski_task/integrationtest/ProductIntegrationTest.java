package com.example.cloudfide_igor_barcikowski_task.integrationtest;

import com.example.cloudfide_igor_barcikowski_task.exception.ResourceDoesNotBelongException;
import com.example.cloudfide_igor_barcikowski_task.producer.Producer;
import com.example.cloudfide_igor_barcikowski_task.producer.ProducerRepository;
import com.example.cloudfide_igor_barcikowski_task.producer.ProducerService;
import com.example.cloudfide_igor_barcikowski_task.product.Product;
import com.example.cloudfide_igor_barcikowski_task.product.ProductRepository;
import com.example.cloudfide_igor_barcikowski_task.product.ProductService;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductCreateRequest;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductPatchRequest;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductResponse;
import com.example.cloudfide_igor_barcikowski_task.productattributes.ProductAttributeRepository;
import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributeRequest;
import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributesPatchRequest;
import com.example.cloudfide_igor_barcikowski_task.utils.dynamicfilter.ProductFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class ProductIntegrationTest {
    @Autowired private ProducerRepository producerRepository;

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductService productService;



    @Test
    void createProductSuccessfully() {
        Set<ProductAttributeRequest> attributes = new HashSet<>(Set.of(
                new ProductAttributeRequest("size", "50 inch"),
                new ProductAttributeRequest("price", "100")
        ));

        ProductCreateRequest productCreateRequest = new ProductCreateRequest("TV", "black", 10, attributes, BigDecimal.valueOf(100));

        Producer producer = producerRepository.save(new Producer("test", "poland", "nice producer"));
        productService.createProduct(productCreateRequest, producer.getId());

        List<Product> products = productRepository.findAll();

        assertThat(products.size()).isEqualTo(1);
        assertThat(products.getFirst().getProductName()).isEqualTo("TV");
    }


    @Test
    void getAllProductsSuccessfully() {
        Set<ProductAttributeRequest> attributes = new HashSet<>(Set.of(
                new ProductAttributeRequest("size", "50 inch"),
                new ProductAttributeRequest("price", "100")
        ));

        ProductCreateRequest productCreateRequest1 = new ProductCreateRequest("TV", "black", 10, attributes, BigDecimal.valueOf(100));
        ProductCreateRequest productCreateRequest2 = new ProductCreateRequest("Phone", "black", 110, attributes, BigDecimal.valueOf(190));


        Producer producer = producerRepository.save(new Producer("test", "poland", "nice producer"));
        productService.createProduct(productCreateRequest1, producer.getId());
        productService.createProduct(productCreateRequest2, producer.getId());

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductResponse> productsAll = productService.getAllProducts(pageable, null);
        assertThat(productsAll.getTotalElements()).isEqualTo(2);

        Page<ProductResponse> productsTV =  productService.getAllProducts(pageable, new ProductFilter("tv", null, null, null));
        assertThat(productsTV.getTotalElements()).isEqualTo(1);
        assertThat(productsTV.getContent().getFirst().productName()).isEqualTo("TV");

        Page<ProductResponse> productsBetween90and110 = productService.getAllProducts(pageable, new ProductFilter(null, BigDecimal.valueOf(90), BigDecimal.valueOf(110),null));
        assertThat(productsBetween90and110.getTotalElements()).isEqualTo(1);
        assertThat(productsBetween90and110.getContent().getFirst().productName()).isEqualTo("TV");

        Page<ProductResponse> productsWithProducerTestAndPhone = productService.getAllProducts(pageable, new ProductFilter("phone", null, null, "test"));
        assertThat(productsWithProducerTestAndPhone.getTotalElements()).isEqualTo(1);
        assertThat(productsWithProducerTestAndPhone.getContent().getFirst().productName()).isEqualTo("Phone");
    }


    @Test
    void patchProductSuccessfully() {
        Set<ProductAttributeRequest> attributes = new HashSet<>(Set.of(
                new ProductAttributeRequest("size", "50 inch"),
                new ProductAttributeRequest("price", "100")
        ));

        ProductCreateRequest productCreateRequest1 = new ProductCreateRequest("TV", "black", 10, attributes, BigDecimal.valueOf(100));


        Producer producer = producerRepository.save(new Producer("test", "poland", "nice producer"));
        ProductResponse savedProduct = productService.createProduct(productCreateRequest1, producer.getId());

        productService.patchProduct(
                new ProductPatchRequest("Phone", null, null, null),
                savedProduct.id()
        );

        Product foundProduct = productRepository.findById(savedProduct.id()).orElseThrow();
        assertThat(foundProduct.getProductName()).isEqualTo("Phone");
        assertThat(foundProduct.getDescription()).isEqualTo("black");
    }


    @Test
    void patchProductAttributesSuccessfully() {
        Set<ProductAttributeRequest> attributes = new HashSet<>(Set.of(
                new ProductAttributeRequest("size", "50 inch"),
                new ProductAttributeRequest("price", "100")
        ));

        ProductCreateRequest productCreateRequest1 = new ProductCreateRequest("TV", "black", 10, attributes, BigDecimal.valueOf(100));


        Producer producer = producerRepository.save(new Producer("test", "poland", "nice producer"));
        ProductResponse savedProduct = productService.createProduct(productCreateRequest1, producer.getId());

        ProductAttributesPatchRequest productAttributesPatchRequest = new ProductAttributesPatchRequest("light", "led");

        ProductResponse attributesPatchedProduct = productService.patchProductAttribute(
                productAttributesPatchRequest,
                savedProduct.id(),
                savedProduct.productAttributes().stream().findFirst().orElseThrow().id()
                );

        assertThat(attributesPatchedProduct.productAttributes())
                .anySatisfy(attribute -> {
                    assertThat(attribute.name()).isEqualTo("light");
                    assertThat(attribute.value()).isEqualTo("led");
                });
    }


    @Test
    void productAttributeDeleteSuccessfully() {
        Set<ProductAttributeRequest> attributes = new HashSet<>(Set.of(
                new ProductAttributeRequest("size", "50 inch"),
                new ProductAttributeRequest("price", "100")
        ));

        ProductCreateRequest productCreateRequest1 = new ProductCreateRequest("TV", "black", 10, attributes, BigDecimal.valueOf(100));


        Producer producer = producerRepository.save(new Producer("test", "poland", "nice producer"));
        ProductResponse savedProduct = productService.createProduct(productCreateRequest1, producer.getId());

        assertThat(productRepository.findAll()).hasSize(1);


        productService.deleteProduct(savedProduct.id());

        assertThat(productRepository.findAll()).hasSize(0);
    }



    @Test
    void productAttributeDeleteThrowsBadRequest() {
        Set<ProductAttributeRequest> attributes1 = new HashSet<>(Set.of(
                new ProductAttributeRequest("size", "50 inch"),
                new ProductAttributeRequest("price", "100")
        ));

        Set<ProductAttributeRequest> attributes2 = new HashSet<>(Set.of(
                new ProductAttributeRequest("size", "50 inch"),
                new ProductAttributeRequest("price", "100")
        ));

        ProductCreateRequest productCreateRequest1 = new ProductCreateRequest("TV", "black", 10, attributes1, BigDecimal.valueOf(100));
        ProductCreateRequest productCreateRequest2 = new ProductCreateRequest("Phone", "black", 110, attributes2, BigDecimal.valueOf(190));


        Producer producer1 = producerRepository.save(new Producer("test", "poland", "nice producer"));
        Producer producer2 = producerRepository.save(new Producer("testdwa", "togo", "bad producer"));

        ProductResponse productResponse1 = productService.createProduct(productCreateRequest1, producer1.getId());
        ProductResponse productResponse2 = productService.createProduct(productCreateRequest2, producer2.getId());


        Long product2FirstAttributeId = productResponse2.productAttributes().stream().findFirst().orElseThrow().id();

        ResourceDoesNotBelongException ex = assertThrows(
                ResourceDoesNotBelongException.class, () -> productService.deleteAttribute(
                        productResponse1.id(),
                        product2FirstAttributeId
                        )
        );

        assertThat(ex.getMessage()).isEqualTo("attribute with id " + product2FirstAttributeId + " does not belong to product with id " + productResponse1.id());
    }



    @Test
    void addAttributeSuccessfully() {
        ProductCreateRequest productCreateRequest1 = new ProductCreateRequest("TV", "black", 10, null, BigDecimal.valueOf(100));


        Producer producer = producerRepository.save(new Producer("test", "poland", "nice producer"));
        ProductResponse savedProduct = productService.createProduct(productCreateRequest1, producer.getId());


        Set<ProductAttributeRequest> attributes = new HashSet<>(Set.of(
                new ProductAttributeRequest("size", "50 inch"),
                new ProductAttributeRequest("price", "100")
        ));

        ProductResponse addedAttributesProduct = productService.addAttributes(savedProduct.id(), attributes);

        assertThat(addedAttributesProduct.productAttributes()).hasSize(2);
        assertThat(addedAttributesProduct.productAttributes()).anySatisfy(
                a -> {
                        assertThat(a.name()).isEqualTo("size");
                        assertThat(a.value()).isEqualTo("50 inch");
                });
    }


    @Test
    void deleteProductSuccessfully() {
        ProductCreateRequest productCreateRequest1 = new ProductCreateRequest("TV", "black", 10, null, BigDecimal.valueOf(100));

        Producer producer = producerRepository.save(new Producer("test", "poland", "nice producer"));
        ProductResponse savedProduct = productService.createProduct(productCreateRequest1, producer.getId());

        productService.deleteProduct(savedProduct.id());

        assertThat(productRepository.findAll()).hasSize(0);
    }
}
