package ru.sber.app.application.product;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sber.app.application.product.mapper.ProductMapper;
import ru.sber.app.domain.entity.Product;
import ru.sber.app.infrastructure.repository.ProductRepository;
import ru.sber.app.presentation.web.controller.product.dto.commands.ProductCommand;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    /**
     * Retrieves all products from the repository.
     *
     * @return a list of all products
     */
    public List<Product> findAll(){
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID from the repository.
     *
     * @param productId the ID of the product to retrieve
     * @return the product with the specified ID
     * @throws EntityNotFoundException if the product with the given ID is not found
     */
    public Product findById(Integer productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> {
            log.error("ProductService | findById | Product with id: " + productId + " not found");
            return new EntityNotFoundException("Продукт с id: " + productId + " не найден");
            }
        );
        return product;

    }
    /**
     * Creates a new product based on the provided command and saves it to the repository.
     *
     * @param command the command containing the information for creating the product
     * @return the newly created product
     */
    public Product create(ProductCommand command) {
        Product product = productMapper.fromCommandToProduct(command);
        return productRepository.save(product);
    }
    /**
     * Updates the product with the specified ID based on the provided command and saves it to the repository.
     *
     * @param productId the ID of the product to update
     * @param command the command containing the information for updating the product
     * @return the updated product
     * @throws EntityNotFoundException if the product with the given ID is not found
     */
    public Product update(Integer productId, ProductCommand command ){
        Product product = productRepository.findById(productId).orElseThrow(() -> {
                    log.error("ProductService | update | Product with id: " + productId + " not found");
                    return new EntityNotFoundException("Продукт с id: " + productId + " не найден");
                }
        );
        Product updateProduct = productMapper.fromCommandToProduct(command);
        updateProduct.setId(productId);
        log.debug(updateProduct.toString());
        return productRepository.save(updateProduct);
    }
    /**
     * Deletes the product with the specified ID from the repository.
     *
     * @param productId the ID of the product to delete
     * @throws EntityNotFoundException if the product with the given ID is not found
     */
    public void delete(Integer productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> {
                    log.error("ProductService | delete | Product with id: " + productId + " not found");
                    return new EntityNotFoundException("Продукт с id: " + productId + " не найден");
                }
        );
        productRepository.delete(product);
    }


}
