package shop.shopBE.domain.destination.repository;

import shop.shopBE.domain.destination.entity.Destination;
import shop.shopBE.domain.destination.request.AddDestinationRequest;
import shop.shopBE.domain.destination.request.UpdateDestinationRequest;
import shop.shopBE.domain.destination.response.DestinationListInfo;

import java.util.List;
import java.util.Optional;

public interface DestinationRepositoryCustom {
    //배송지 조회
    Optional<List<DestinationListInfo>> findDestinationListByMemberId(Long memberId);
}
