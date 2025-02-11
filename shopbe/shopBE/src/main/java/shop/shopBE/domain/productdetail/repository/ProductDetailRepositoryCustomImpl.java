package shop.shopBE.domain.productdetail.repository;

import com.querydsl.core.QueryFactory;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

import shop.shopBE.domain.productdetail.entity.ProductDetail;
import shop.shopBE.domain.productdetail.entity.QProductDetail;
import shop.shopBE.domain.productdetail.response.ProductDetails;

import java.util.List;
import java.util.Optional;

import static shop.shopBE.domain.productdetail.entity.QProductDetail.*;

@RequiredArgsConstructor
public class ProductDetailRepositoryCustomImpl implements ProductDetailRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<List<ProductDetails>> findProductDetailsByProductId(Long productId) {

        List<ProductDetails> productDetailsList = queryFactory.select(Projections.constructor(ProductDetails.class,
                productDetail.id,
                productDetail.shoesSize,
                productDetail.sizeStock))
                .from(productDetail)
                .where(productDetail.product.id.eq(productId))
                .fetch();

        return Optional.ofNullable(productDetailsList);
    }

    @Override
<<<<<<< HEAD
    public Optional<Integer> findQuantityByProductIdAndSize(Long productId, int size) {
        Integer findQuantity = queryFactory
                .select(productDetail.sizeStock)
                .from(productDetail)
                .where(productDetail.product.id.eq(productId), productDetail.shoesSize.eq(size))
                .fetchOne();

        return Optional.ofNullable(findQuantity);
=======
    public Optional<ProductDetail> findByProductIdAndSize(Long productId, int size) {

        ProductDetail result = queryFactory
                .select(productDetail)
                .from(productDetail)
                .where(productDetail.product.id.eq(productId).and(productDetail.shoesSize.eq(size)))
                .fetchOne();

        return Optional.ofNullable(result);

>>>>>>> 20bd19f28fe0ea88fbdd9f1c904144853b17f387
    }
}
