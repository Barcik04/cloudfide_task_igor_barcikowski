package com.example.cloudfide_igor_barcikowski_task.product;

import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductCreateRequest;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductPatchRequest;
import com.example.cloudfide_igor_barcikowski_task.product.dto.ProductResponse;
import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributeRequest;
import com.example.cloudfide_igor_barcikowski_task.productattributes.dto.ProductAttributesPatchRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(
            summary = "Creates a product with given list of attributes")
    @PostMapping("/producer/{producerId}")
    public ResponseEntity<ProductResponse> createProducts(
            @RequestBody @Valid ProductCreateRequest  productCreateRequest,
            @PathVariable @Positive Long producerId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProducts(productCreateRequest, producerId));
    }



    @Operation(
            summary = "patching product fields (excluding attributes) by its id")
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> patchProduct(
            @RequestBody @Valid ProductPatchRequest productPatchRequest,
            @PathVariable @Positive Long productId
            ) {
        return ResponseEntity.ok(productService.patchProduct(productPatchRequest, productId));
    }




    @Operation(
            summary = "patching attribute fields by specifying productId and attributeId")
    @PatchMapping("/product/{productId}/attributes/{attributeId}")
    public ResponseEntity<ProductResponse> patchProductAttribute(
            @PathVariable @Positive Long productId,
            @PathVariable @Positive Long attributeId,
            @RequestBody @Valid ProductAttributesPatchRequest productAttributesPatchRequest
    ) {
        return ResponseEntity.ok(productService.patchProductAttribute(productAttributesPatchRequest, productId, attributeId));
    }



    @Operation(
            summary = "Deletes attribute from a product specified by productId and attributeId")
    @DeleteMapping("/product/{productId}/attribute/{attributeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductAttribute(
            @PathVariable @Positive Long productId,
            @PathVariable @Positive Long attributeId
    ) {
         productService.deleteAttribute(productId, attributeId);
    }



    @Operation(
            summary = "Adding attribute to product by given productId")
    @PostMapping("/{productId}")
    public ResponseEntity<ProductResponse> addAttributeToProduct(
            @PathVariable @Positive Long productId,
            @RequestBody @Valid ProductAttributeRequest productAttributeRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addAttribute(productId, productAttributeRequest));
    }


    @Operation(
            summary = "Deletes product by given productId")
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @PathVariable @Positive Long productId
    ) {
        productService.deleteProduct(productId);
    }
}
