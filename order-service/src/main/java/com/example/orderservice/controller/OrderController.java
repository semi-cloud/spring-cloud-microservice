package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {
    private final Environment env;
    private final OrderService orderService;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder requestOrder,
                                                    @PathVariable("userId") String userId){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createdOrder = orderService.createOrder(orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(createdOrder, ResponseOrder.class));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@RequestBody RequestOrder requestOrder,
                                                           @PathVariable("userId") String userId){
        List<OrderEntity> orders = orderService.getOrdersByUserId(userId);
        List<ResponseOrder> result = orders.stream().map(x -> new ModelMapper().map(x, ResponseOrder.class)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/health_check")
    public String status(){
        return String.format("It's Working in User Service on port %s",
                env.getProperty("local.server.port"));
    }
}
