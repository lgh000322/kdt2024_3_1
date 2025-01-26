package shop.shopBE.domain.product.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import shop.shopBE.domain.product.entity.enums.PersonCategory;
import shop.shopBE.domain.product.entity.enums.ProductCategory;
import shop.shopBE.domain.product.entity.enums.SeasonCategory;
import shop.shopBE.domain.product.request.SortingOption;
import shop.shopBE.domain.product.response.ProductCardViewModel;
import shop.shopBE.domain.product.response.ProductListViewModel;
import shop.shopBE.domain.productimage.entity.enums.ProductImageCategory;

import java.util.List;
import java.util.Optional;

import static shop.shopBE.domain.likesitem.entity.QLikesItem.likesItem;
import static shop.shopBE.domain.product.entity.QProduct.product;
import static shop.shopBE.domain.productimage.entity.QProductImage.productImage;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public ProductListViewModel getProductListViewModels(Long productId) {
        return queryFactory
                .select(Projections.constructor(ProductListViewModel.class,
                        product.id,
                        likesItem.id,
                        productImage.savedName,
                        product.productName
                ))
                .from(product)
                .innerJoin(productImage).on(product.id.eq(productImage.product.id))
                .where(productImage.productImageCategory.eq(ProductImageCategory.MAIN))
                .fetchOne();
    }


    // 메인페이지 프로덕트뷰 - 인기순으로 desc하여 반환.
    @Override
    public Optional<List<ProductCardViewModel>> findMainProductCardsOderByLikeCountDesc(Pageable pageable) {
        List<ProductCardViewModel> productCardViewModels = queryFactory
                .select(Projections.constructor(ProductCardViewModel.class,
                        product.id,
                        productImage.savedName,
                        product.productName,
                        product.price))
                .from(product)
                .join(productImage)
                .on(productImage.product.eq(product))
                .where(productImage.productImageCategory.eq(ProductImageCategory.MAIN))
                .orderBy(product.likeCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return Optional.ofNullable(productCardViewModels);
    }

    // 시즌별 상품 조회 메서드 - 인기순 desc
    @Override
    public Optional<List<ProductCardViewModel>> findSeasonProductsOrderByLikeCountDesc(Pageable pageable,
                                                                                       SeasonCategory seasonCategory) {
        List<ProductCardViewModel> productCardVies = queryFactory
                .select(Projections.constructor(ProductCardViewModel.class,
                        product.id,
                        productImage.savedName,
                        product.productName,
                        product.price))
                .from(product)
                .join(productImage)
                .on(productImage.product.eq(product))
                .where(product.seasonCategory.eq(seasonCategory)
                        .and(productImage.productImageCategory.eq(ProductImageCategory.MAIN)))
                .orderBy(product.likeCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return Optional.ofNullable(productCardVies);
    }

    // 시즌별 상품 조회 메서드 - 판매량순
    @Override
    public Optional<List<ProductCardViewModel>> findSeasonProductsOrderBySalesVolumeDesc(Pageable pageable,
                                                                                         SeasonCategory seasonCategory) {
        List<ProductCardViewModel> productCardVies = queryFactory
                .select(Projections.constructor(ProductCardViewModel.class,
                        product.id,
                        productImage.savedName,
                        product.productName,
                        product.price))
                .from(product)
                .join(productImage)
                .on(productImage.product.eq(product))
                .where(product.seasonCategory.eq(seasonCategory)
                        .and(productImage.productImageCategory.eq(ProductImageCategory.MAIN)))
                .orderBy(product.salesVolume.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return Optional.ofNullable(productCardVies);
    }

    // 시즌별 상품 조회 메서드 - 신상품순(입고순)
    @Override
    public Optional<List<ProductCardViewModel>> findSeasonProductsOrderByCreateAtDesc(Pageable pageable,
                                                                                      SeasonCategory seasonCategory) {
        List<ProductCardViewModel> productCardVies = queryFactory
                .select(Projections.constructor(ProductCardViewModel.class,
                        product.id,
                        productImage.savedName,
                        product.productName,
                        product.price))
                .from(product)
                .join(productImage)
                .on(productImage.product.eq(product))
                .where(product.seasonCategory.eq(seasonCategory)
                        .and(productImage.productImageCategory.eq(ProductImageCategory.MAIN)))
                .orderBy(product.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return Optional.ofNullable(productCardVies);
    }

    // 시즌별 상품 조회 메서드 - 낮은가격순
    @Override
    public Optional<List<ProductCardViewModel>> findSeasonProductsOrderByPriceAsc(Pageable pageable, SeasonCategory seasonCategory) {
        List<ProductCardViewModel> productCardVies = queryFactory
                .select(Projections.constructor(ProductCardViewModel.class,
                        product.id,
                        productImage.savedName,
                        product.productName,
                        product.price))
                .from(product)
                .join(productImage)
                .on(productImage.product.eq(product))
                .where(product.seasonCategory.eq(seasonCategory)
                        .and(productImage.productImageCategory.eq(ProductImageCategory.MAIN)))
                .orderBy(product.price.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return Optional.ofNullable(productCardVies);
    }


    // 남성 카테고리별 상품조회 - 좋아요순
    @Override
    public Optional<List<ProductCardViewModel>> findPersonProductsOrderByLikeCountDesc(Pageable pageable,
                                                                                       PersonCategory personCategory,
                                                                                       ProductCategory productCategory) {

        List<ProductCardViewModel> productCardViewModels = queryFactory
                .select(Projections.constructor(ProductCardViewModel.class,
                        product.id,
                        productImage.savedName,
                        product.productName,
                        product.price))
                .from(product)
                .join(productImage)
                .on(productImage.product.eq(product))
                .where(product.personCategory.eq(personCategory)
                        .and(product.productCategory.eq(productCategory))
                        .and(productImage.productImageCategory.eq(ProductImageCategory.MAIN)))
                .orderBy(product.likeCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return Optional.ofNullable(productCardViewModels);
    }

    // 남성 카테고리별 상품조회 - 판매량
    @Override
    public Optional<List<ProductCardViewModel>> findPersonProductsOrderBySalesVolumeDesc(Pageable pageable, PersonCategory personCategory) {
        return Optional.empty();
    }

    // 남성 카테고리별 상품조회 - 신상품순
    @Override
    public Optional<List<ProductCardViewModel>> findPersonProductsOrderByCreateAtDesc(Pageable pageable, PersonCategory personCategory) {
        return Optional.empty();
    }

    // 남성 카테고리별 상품조회 - 낮은가격순
    @Override
    public Optional<List<ProductCardViewModel>> findPersonProductsOrderByPriceAsc(Pageable pageable, PersonCategory personCategory) {
        return Optional.empty();
    }
}
