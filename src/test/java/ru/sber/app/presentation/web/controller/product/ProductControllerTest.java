package ru.sber.app.presentation.web.controller.product;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sber.app.application.product.ProductService;
import ru.sber.app.application.product.mapper.ProductMapper;
import ru.sber.app.domain.entity.Product;
import ru.sber.app.infrastructure.repository.ProductRepository;
import ru.sber.app.presentation.web.controller.product.ProductController;
import ru.sber.app.presentation.web.controller.product.dto.commands.ProductCommand;
import ru.sber.app.presentation.web.controller.product.dto.queries.ProductQuery;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@DisplayName("Модульные тесты ProductController")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProductControllerTest {
    ProductMapper mapper = mock(ProductMapper.class);
    ProductRepository repo = mock(ProductRepository.class);
    ProductService service = mock(ProductService.class);
    ProductController controller = new ProductController(service, mapper);



    @Test
    @DisplayName("CreateProduct создаст новый товар и вернет дто")
    void createProduct_RequestIsValid_ReturnsProductQuery(){
        //given
        ProductCommand command = ProductCommand.builder().description("sofa").price(11.2F).title("new sofa").build();

        ProductQuery expected = ProductQuery.builder().title("sofa").price(11.2F).description("new sofa").build();

        Product product = Product.builder().id(1).title("sofa").price(11.2F).description("new sofa").build();

        //when
        when(repo.save(notNull())).thenReturn(product);
        when(service.create(command)).thenReturn(product);
        when(mapper.fromProductToQuery(product)).thenReturn(expected);
        var result = this.controller.createProduct(command);
        //then
        assertEquals(expected, result);
        verify(service).create(command);
        verify(mapper).fromProductToQuery(product);
        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(mapper);
    }
    @Test
    @DisplayName("FindAll - найдет все товары в базе")
    void findAll_RequestIsValid_ReturnsListProductQuery(){
        //given
        Product product1 = Product.builder().id(1).title("sofa").price(1F).description("new sofa").build();
        Product product2 = Product.builder().id(1).title("chair").price(2F).description("new chair").build();
        List<Product> products = List.of(product1, product2);
        ProductQuery query1 = ProductQuery.builder().id(1).title("sofa").price(1F).description("new sofa").build();
        ProductQuery query2 = ProductQuery.builder().id(1).title("chair").price(2F).description("new chair").build();
        List<ProductQuery> queries = List.of(query1, query2);
        //when
        when(service.findAll()).thenReturn(products);
        when(repo.findAll()).thenReturn(products);
        when(mapper.fromProductsToQueries(products)).thenReturn(queries);

        var result = controller.findAll();

        //then
        assertEquals(queries, result);
        verify(service).findAll();
        verify(mapper).fromProductsToQueries(products);
        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(mapper);
    }
    @Test
    @DisplayName("findProductById найдет товар по id и вернет дто")
    void findProductById_RequestIsValid_ReturnsProductQuery(){
        Integer id = 1;
        Product product = Product.builder().id(1).title("sofa").price(1F).description("new sofa").build();
        ProductQuery query = ProductQuery.builder().id(1).title("sofa").price(1F).description("new sofa").build();
        //given

        //when
        when(service.findById(id)).thenReturn(product);
        when(mapper.fromProductToQuery(product)).thenReturn(query);
        var result = controller.findProductById(id);

        //then
        assertEquals(query, result);
        verify(service).findById(id);
        verify(mapper).fromProductToQuery(product);
        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("findProductById товар по id не найден будет выброшено исключение")
    void findProductById_NoSuchProduct_throwsException(){
        //given
        Integer id = 1;

        //when
        when(repo.findById(id)).thenReturn(null);
        when(service.findById(id)).thenThrow(EntityNotFoundException.class);

        //then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            controller.findProductById(id);
        });

        verify(service).findById(id);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(mapper);
    }
    @Test
    @DisplayName("updateProduct найдет товар по id изменит его и вернет дто")
    void updateProduct_RequestIsValid_ReturnsProductQuery(){
        //given
        Integer id = 1;
        Product product = Product.builder().id(1).title("sofa").price(1F).description("new sofa").build();
        ProductQuery query = ProductQuery.builder().id(1).title("sofa").price(1F).description("new sofa").build();
        ProductCommand command = ProductCommand.builder().description("new sofa").price(1F).title("sofa").build();

        //when
        when(service.update(id, command)).thenReturn(product);
        when(mapper.fromProductToQuery(product)).thenReturn(query);
        var result = controller.updateProduct(id, command);

        //then
        assertEquals(query, result);
        verify(service).update(id, command);
        verify(mapper).fromProductToQuery(product);
        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("updateProduct товар с таким id не найден будет выброшено исключение")
    void updateProduct_NoSuchProduct_throwsException(){
        //given
        Integer id = 1;
        ProductCommand command = ProductCommand.builder().description("new sofa").price(1F).title("sofa").build();
        //when
        when(repo.findById(id)).thenReturn(null);
        when(service.update(id, command)).thenThrow(EntityNotFoundException.class);

        //then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            controller.updateProduct(id, command);
        });

        verify(service).update(id, command);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(mapper);
    }
    @Test
    @DisplayName("deleteProduct найдет товар с таким id и удалит его")
    void deleteProduct_RequestIsValid_ReturnsNothing(){
        //given
        Integer id = 1;
        //when
        controller.deleteProduct(id);
        //then
        verify(service).delete(id);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("deleteProduct товар с таким id не найден будет выброшено исключение")
    void deleteProduct_NoSuchProduct_throwsException(){
        //given
        Integer id = 1;
        //when
        doThrow(EntityNotFoundException.class).when(service).delete(id);

        //then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            controller.deleteProduct(id);
        });

        verify(service).delete(id);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(mapper);
    }



}
