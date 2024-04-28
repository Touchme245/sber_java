package ru.sber.app.application.product;

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
import ru.sber.app.presentation.web.controller.product.dto.commands.ProductCommand;
import ru.sber.app.presentation.web.controller.product.dto.queries.ProductQuery;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@DisplayName("Модульные тесты ProductService")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProductServiceTest {
    ProductMapper mapper = mock(ProductMapper.class);
    ProductRepository repo = mock(ProductRepository.class);
    ProductService service = new ProductService(repo,mapper);

    @Test
    @DisplayName("findAll вернет все доступные товары")
    void findAll_RequestIsValid_ReturnsListProductQueries(){
        //given
        Product product1 = Product.builder().id(1).title("sofa").price(1F).description("new sofa").build();
        Product product2 = Product.builder().id(1).title("chair").price(2F).description("new chair").build();
        List<Product> products = List.of(product1, product2);

        //when
        when(repo.findAll()).thenReturn(products);
        var result = service.findAll();

        //then
        assertEquals(products, result);
        verify(repo).findAll();
        verifyNoMoreInteractions(repo);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("findById вернет товар из базы по id")
    void findById_RequestIsValid_ReturnsProductQuery(){
        //given
        Integer id = 1;
        Product product = Product.builder().id(1).title("sofa").price(1F).description("new sofa").build();

        //when
        when(repo.findById(id)).thenReturn(Optional.of(product));
        var result = service.findById(id);

        //then
        assertEquals(product, result);
        verify(repo).findById(id);
        verifyNoMoreInteractions(repo);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("findById продукт с таким id не найден будет выброшено исключение")
    void findById_NoSuchProduct_ThrowsException(){
        //given
        Integer id = 1;

        //when
        when(repo.findById(id)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.findById(id);
        });
        verify(repo).findById(id);
        verifyNoMoreInteractions(repo);
        verifyNoInteractions(mapper);
    }
    @Test
    @DisplayName("create создаст новый товар вернет Product")
    void create_RequestIsValid_ReturnsProduct(){
        //given
        Product product = Product.builder().id(1).title("sofa").price(1F).description("new sofa").build();
        ProductCommand command = ProductCommand.builder().title("sofa").price(1F).description("new sofa").build();

        //when
        when(mapper.fromCommandToProduct(command)).thenReturn(product);
        when(repo.save(product)).thenReturn(product);
        var result = service.create(command);
        //then
        assertEquals(product, result);
        verify(repo).save(product);
        verify(mapper).fromCommandToProduct(command);
        verifyNoMoreInteractions(repo);
        verifyNoMoreInteractions(mapper);

    }


    @Test
    @DisplayName("update обновит товар вернет Product")
    void update_RequestIsValid_ReturnsProduct(){
        //given
        Product product2 = Product.builder().id(1).title("sofa").price(2F).description("new sofa").build();
        Product product1 = Product.builder().id(1).title("chair").price(1F).description("new chair").build();
        ProductCommand command = ProductCommand.builder().title("chair").price(1F).description("new chair").build();

        //when
        when(repo.findById(product2.getId())).thenReturn(Optional.of(product2));
        when(mapper.fromCommandToProduct(command)).thenReturn(product1);
        when(repo.save(product1)).thenReturn(product1);

        var result = service.update(1, command);
        //then
        assertEquals(product1, result);
        verify(repo).findById(1);
        verify(repo).save(product1);
        verify(mapper).fromCommandToProduct(command);
        verifyNoMoreInteractions(repo);
        verifyNoMoreInteractions(mapper);

    }

    @Test
    @DisplayName("update продукт с таким id не найден будет выброшено исключение")
    void update_NoSuchProduct_ThrowsException(){
        //given
        Integer id = 1;
        ProductCommand command = ProductCommand.builder().title("sofa").price(1F).description("new sofa").build();

        //when
        when(repo.findById(id)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.update(id, command);
        });
        verify(repo).findById(id);
        verifyNoMoreInteractions(repo);
        verifyNoInteractions(mapper);
    }
    @Test
    @DisplayName("delete удаляет товар по id ничего не возвращает")
    void delete_RequestIsValid_ReturnNothing(){
        //given
        Integer id = 1;
        Product product1 = Product.builder().id(1).title("sofa").price(1F).description("new sofa").build();

        //when
        when(repo.findById(id)).thenReturn(Optional.of(product1));
        service.delete(id);

        //then
        verify(repo).findById(id);
        verify(repo).delete(product1);

    }

    @Test
    @DisplayName("delete продукт с таким id не найден будет выброшено исключение")
    void delete_NoSuchProduct_ThrowsException(){
        //given
        Integer id = 1;

        //when
        when(repo.findById(id)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.delete(id);
        });
        verify(repo).findById(id);
        verifyNoMoreInteractions(repo);
        verifyNoInteractions(mapper);
    }
}
