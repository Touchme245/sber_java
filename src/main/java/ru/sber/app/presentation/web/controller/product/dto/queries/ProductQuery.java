package ru.sber.app.presentation.web.controller.product.dto.queries;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductQuery {
    private Integer id;

    private String title;

    private String description;

    private Float price;
}
