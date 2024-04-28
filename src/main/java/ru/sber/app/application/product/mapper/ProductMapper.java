package ru.sber.app.application.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.sber.app.domain.entity.Product;
import ru.sber.app.presentation.web.controller.product.dto.commands.ProductCommand;
import ru.sber.app.presentation.web.controller.product.dto.queries.ProductQuery;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
//    Product fromCommandToProduct()
    ProductQuery fromProductToQuery(Product product);

    List<ProductQuery> fromProductsToQueries(List<Product> products);

    Product fromCommandToProduct(ProductCommand command);
}
