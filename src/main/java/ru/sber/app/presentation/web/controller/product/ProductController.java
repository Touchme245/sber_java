package ru.sber.app.presentation.web.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.app.application.product.mapper.ProductMapper;
import ru.sber.app.application.product.ProductService;
import ru.sber.app.domain.entity.Product;
import ru.sber.app.presentation.web.controller.product.dto.commands.ProductCommand;
import ru.sber.app.presentation.web.controller.product.dto.queries.ProductQuery;
import java.util.List;
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    @GetMapping()
    public List<ProductQuery> findAll(){
        List<Product> products = productService.findAll();
        return productMapper.fromProductsToQueries(products);

    }
    @GetMapping("/{id}")
    public ProductQuery findProductById(@PathVariable Integer id){
        Product product = productService.findById(id);
        return productMapper.fromProductToQuery(product);

    }
    @PostMapping()
    public ProductQuery createProduct(@RequestBody ProductCommand command){
        Product createdProduct = productService.create(command);
        return productMapper.fromProductToQuery(createdProduct);
    }
    @PutMapping("/{id}")
    public ProductQuery updateProduct(@PathVariable Integer id ,@RequestBody ProductCommand command){
        Product updatedProduct = productService.update(id, command);
        return productMapper.fromProductToQuery(updatedProduct);
    }
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id){
        productService.delete(id);

    }




}
