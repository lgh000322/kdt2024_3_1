package shop.shopBE.domain.orderproduct.response;


import java.util.List;

public record OrderProductInfo(
        Long orderHistoryId, //주문내역 번호
        String orderName, //주문자 이름
        String deliveryAddress, //주문주소
        String phoneNumber, //주문자 전화번호
        List<DetailOrderProducts> orderDetailProducts
) { }
