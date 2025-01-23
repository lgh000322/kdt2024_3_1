package shop.shopBE.domain.orderproduct.repository;

import shop.shopBE.domain.orderproduct.request.OrderProductInfo;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepositoryCustom {

    Optional<List<OrderProductInfo>> findOrderProductByHistoryId(Long historyId);
}
