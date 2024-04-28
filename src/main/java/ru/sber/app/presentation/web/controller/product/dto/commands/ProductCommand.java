package ru.sber.app.presentation.web.controller.product.dto.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCommand {

    private String title;

    private String description;

    private Float price;
}
