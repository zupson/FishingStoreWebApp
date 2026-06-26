package hr.algebra.fishingstore.config;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.dto.ProductOrderDto;
import hr.algebra.fishingstore.model.entities.Product;
import hr.algebra.fishingstore.model.entities.ProductOrder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(ProductDto.EditDto.class, Product.class)
                .setPropertyCondition(context -> !context.getMapping()
                        .getLastDestinationProperty()
                        .getName()
                        .equals("category"));

        modelMapper.createTypeMap(ProductDto.CreateDto.class, Product.class)
                .setPropertyCondition(context -> !context.getMapping()
                        .getLastDestinationProperty()
                        .getName()
                        .equals("category"));


        return modelMapper;
    }
}