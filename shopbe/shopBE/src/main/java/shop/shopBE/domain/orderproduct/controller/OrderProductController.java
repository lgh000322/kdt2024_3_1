package shop.shopBE.domain.orderproduct.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.shopBE.domain.orderproduct.entity.request.OrderProductDeliveryInfo;
import shop.shopBE.domain.orderproduct.response.OrderProductInfo;
import shop.shopBE.domain.orderproduct.service.OrderProductService;
import shop.shopBE.global.response.ResponseFormat;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "주문상품", description = "주문상품 관련 API")
public class OrderProductController {

    private final OrderProductService orderProductService;

    @GetMapping("/orderProduct/{orderProductId}")
    @Operation(summary = "주문상품 상세조회", description = "현재 로그인한 회원의 주문상품 상세조회")
    public ResponseEntity<ResponseFormat<OrderProductInfo>> findOrderProduct(@PathVariable(name = "orderProductId") Long orderProductId) {
        OrderProductInfo findDetailOrderProduct = orderProductService.findDetailOrderProductByHistoryId(orderProductId);
        return ResponseEntity.ok().body(ResponseFormat.of("주문 상세조회 성공", findDetailOrderProduct));
    }

    @PatchMapping("/orderProduct/{orderProductId}")
    @Operation(summary = "주문상품 배송상태 업데이트", description = "현재 로그인한 회원의 주문상품 배송상태를 업데이트")
    public ResponseEntity<ResponseFormat<List<OrderProductInfo>>> UpdateOrderProductDeliveryState( @PathVariable(name = "orderProductId") Long orderProductId,
                                                                                                   @RequestBody @Valid OrderProductDeliveryInfo orderProductDeliveryInfo) {
        orderProductService.UpdateOrderProductDeliveryState(orderProductId, orderProductDeliveryInfo);
        return ResponseEntity.ok().body(ResponseFormat.of("주문 상품 배송상태 업데이트 성공"));
    }

    @GetMapping("orderProduct/deliverySearch/{orderProductID}")
    @Operation(summary = "주문상품 배송조회", description = "현재 로그인한 회원이 주문한 상품의 배송조회")
    public ResponseEntity<ResponseFormat<List<OrderProductDeliveryInfo>>> findOrderProductState(@PathVariable(name = "orderProductID") Long orderProductID){
        List<OrderProductDeliveryInfo> deliveryStatusHistoryList = orderProductService.getOrderProductDeliveryListByHistoryId(orderProductID);
        return ResponseEntity.ok().body(ResponseFormat.of("주문 상품 배송조회 성공", deliveryStatusHistoryList));
    }
}
