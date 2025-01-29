package shop.shopBE.domain.product.service;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.shopBE.domain.product.entity.Product;
import shop.shopBE.domain.product.entity.enums.PersonCategory;
import shop.shopBE.domain.product.entity.enums.ProductCategory;
import shop.shopBE.domain.product.entity.enums.SeasonCategory;
import shop.shopBE.domain.product.exception.ProductExceptionCode;
import shop.shopBE.domain.product.repository.ProductRepository;
import shop.shopBE.domain.product.request.ProductPaging;
import shop.shopBE.domain.product.request.SortingOption;
import shop.shopBE.domain.product.response.ProductCardViewModel;
import shop.shopBE.domain.product.response.ProductInformsModelView;
import shop.shopBE.domain.product.response.ProductListViewModel;
import shop.shopBE.domain.productdetail.response.ProductDetails;
import shop.shopBE.domain.productdetail.service.ProductDetailService;
import shop.shopBE.domain.productimage.entity.ProductImage;
import shop.shopBE.domain.productimage.entity.enums.ProductImageCategory;
import shop.shopBE.domain.productimage.service.ProductImageService;
import shop.shopBE.global.exception.custom.CustomException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailService productDetailService;
    private final ProductImageService productImageService;

    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ProductExceptionCode.NOT_FOUND));
    }

    // 메인페이지 프로덕트 카드뷰 - 인기순(좋아요 숫자) 내림차순
    public List<ProductCardViewModel> findMainPageCardViews(ProductPaging productPaging) {

        Pageable pageable = PageRequest.of(productPaging.page() - 1, productPaging.size());

        List<ProductCardViewModel> mainProductCardViews = productRepository.findMainProductCardsOderByLikeCountDesc(pageable)
                .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));

        return mainProductCardViews;
    }

    // 시즌(여름, 겨울) 클릭시 조회 메서드 (인기순 조회)
    public List<ProductCardViewModel> findSeasonProductInforms(ProductPaging productPaging, SeasonCategory seasonCategory) {
        Pageable pageable = PageRequest.of(productPaging.page() - 1, productPaging.size());

        List<ProductCardViewModel> productCardViewModels = productRepository
                .findSeasonProductsOrderByLikeCountDesc(pageable, seasonCategory)
                .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));

        return productCardViewModels;
    }

    // 시즌 상품 옵션별 상품조회
    public List<ProductCardViewModel> findSeasonProductInformsByOption(ProductPaging productPaging,
                                                                       SeasonCategory seasonCategory,
                                                                       String option) {
        Pageable pageable = PageRequest.of(productPaging.page() - 1, productPaging.size());


        return getFilteredSeasonProductsByOption(seasonCategory, option, pageable);
    }



    //사람(남, 여, 아동) 카테고리 - 상품카테고리별 상품 조회(인기순)
    public List<ProductCardViewModel> findPersonProductInformsByProductCategory(ProductPaging productPaging, PersonCategory personCategory, String productCategory) {

        Pageable pageable =  PageRequest.of(productPaging.page() - 1, productPaging.size());

        ProductCategory checkedProductCategory = validProductCategory(productCategory);

        List<ProductCardViewModel> productCardViewModels = productRepository
                .findPersonProductsOrderByLikeCountDesc(pageable, personCategory, checkedProductCategory)
                .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));

        return productCardViewModels;
    }


    //사람(남, 여, 아동)아래 상품카테고리(슬리퍼, 부츠, 운동화 등등)아래 정렬조건(낮은가격, 인기순, 판매순 등) 조회
    public List<ProductCardViewModel> findPersonProductInformsByOption(@Valid ProductPaging productPaging,
                                                                       PersonCategory personCategory,
                                                                       String productCategory,
                                                                       String option) {

        Pageable pageable = PageRequest.of(productPaging.page() - 1, productPaging.size());

        ProductCategory checkedProductCategory = validProductCategory(productCategory);

        return getFilteredPersonProductsByOption(personCategory, option, pageable, checkedProductCategory);
    }


    // 상품의 상세페이지 정보들을 조회하는 메서드
    // 리스트로 받아야하는 sideImageUrl과 productDetail의 사이즈별 id, 사이즈, 사이즈별 재고는 외부에서 입력받음
    public ProductInformsModelView findProductDetailsByProductId(Long productId) {

        ProductInformsModelView productInforms = productRepository
                .findProductInformsByProductId(productId)
                .orElseThrow(() -> new CustomException(ProductExceptionCode.NOT_FOUND));
        List<String> sideImgs = productImageService.findSideImgsByProductId(productId);
        List<ProductDetails> productDetailsList = productDetailService.findProductDetailsByProductId(productId);

        productInforms.setSideImgUrl(sideImgs);
        productInforms.setProductDetailsList(productDetailsList);

        return productInforms;
    }


    public List<ProductListViewModel> getProductListViewModels(List<Long> productIds) {
        return productIds.stream()
                .map(productRepository::getProductListViewModels)
                .toList();
    }


    // =======================================검증 로직 ========================================================//

    // 상품 카테고리 확인 메서드
    private ProductCategory validProductCategory(String productCategory) {

        //프로덕트 카테고리 이넘값으로 배열을 만듦.
        ProductCategory[] productCategories = ProductCategory.values();

        // 파라미터로 받은 문자열과 일치하는 productCategory를 찾음
        for (ProductCategory category : productCategories) {
            if(category.toString().equals(productCategory)) {
                return category;
            }
        }

        // 없으면 에러를 터트려줌.
        throw new CustomException(ProductExceptionCode.INVALID_PRODUCT_CATEGORY);
    }


    // 옵션(낮은가격순, 인기순등) 으로 필터링한 시즌상품을 반환
    private List<ProductCardViewModel> getFilteredSeasonProductsByOption(SeasonCategory seasonCategory, String option, Pageable pageable) {
        // 낮은가격순 조회일경우
        if(option.equals(SortingOption.LOW_PRICE.toString())){
            return productRepository
                    .findSeasonProductsOrderByPriceAsc(pageable, seasonCategory)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 신상품순 조회일 경우
        if(option.equals(SortingOption.NEW_PRODUCT.toString())){
            return productRepository
                    .findSeasonProductsOrderByCreateAtDesc(pageable, seasonCategory)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 판매량순 조회일경우
        if(option.equals(SortingOption.BEST_SELLERS.toString())){
            return productRepository
                    .findSeasonProductsOrderBySalesVolumeDesc(pageable, seasonCategory)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 인기순 조회일경우
        if(option.equals(SortingOption.POPULAR.toString())){
            return productRepository.findSeasonProductsOrderByLikeCountDesc(pageable, seasonCategory)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }

        //위 조건에 걸리지 않으면 예외처리
        throw new CustomException(ProductExceptionCode.INVALID_OPTION);
    }

    // 옵션으로 필터링한 사람카테고리별 상품 반환
    private List<ProductCardViewModel> getFilteredPersonProductsByOption(PersonCategory personCategory,
                                                                         String option,
                                                                         Pageable pageable,
                                                                         ProductCategory checkedProductCategory) {
        // 낮은가격순 조회일경우
        if(option.equals(SortingOption.LOW_PRICE.toString())){
            return productRepository
                    .findPersonProductsOrderByPriceAsc(pageable, personCategory, checkedProductCategory)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 신상품순 조회일 경우
        if(option.equals(SortingOption.NEW_PRODUCT.toString())){
            return productRepository
                    .findPersonProductsOrderByCreateAtDesc(pageable, personCategory, checkedProductCategory)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 판매량순 조회일경우
        if(option.equals(SortingOption.BEST_SELLERS.toString())){
            return productRepository
                    .findPersonProductsOrderBySalesVolumeDesc(pageable, personCategory, checkedProductCategory)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 인기순 조회일경우
        if(option.equals(SortingOption.POPULAR.toString())){
            return productRepository
                    .findPersonProductsOrderByLikeCountDesc(pageable, personCategory, checkedProductCategory)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }

        //위 조건에 걸리지 않으면 예외처리
        throw new CustomException(ProductExceptionCode.INVALID_OPTION);
    }


/*

    // 남자상품 - 상품카테고리별 조회(인기순)
    public List<ProductCardViewModel> findMenProductInformsByProductCategory(ProductPaging productPaging, String productCategory) {

        Pageable pageable =  PageRequest.of(productPaging.page() - 1, productPaging.size());

        ProductCategory checkedProductCategory = validProductCategory(productCategory);

        List<ProductCardViewModel> productCardViewModels = productRepository
                .findPersonProductsOrderByLikeCountDesc(pageable, PersonCategory.MEN, checkedProductCategory)
                .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));

        return productCardViewModels;
    }

    // 여성 상품 - 상품 카테고리별 조회 (인기순0
    public List<ProductCardViewModel> findWomenProductInformsByProductCategory(ProductPaging productPaging, String productCategory) {

        Pageable pageable =  PageRequest.of(productPaging.page() - 1, productPaging.size());

        ProductCategory checkedProductCategory = validProductCategory(productCategory);

        List<ProductCardViewModel> productCardViewModels = productRepository
                .findPersonProductsOrderByLikeCountDesc(pageable, PersonCategory.WOMEN, checkedProductCategory)
                .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));

        return productCardViewModels;
    }


    // 여름상품 옵션별 조회
    public List<ProductCardViewModel> findSummerProductInformsByOption(ProductPaging productPaging, String option) {

        Pageable pageable = PageRequest.of(productPaging.page() - 1, productPaging.size());

        // 낮은가격순 조회일경우
        if(option.equals(SortingOption.LOW_PRICE.toString())){
            return productRepository
                    .findSeasonProductsOrderByPriceAsc(pageable, SeasonCategory.SUMMER)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 신상품순 조회일 경우
        if(option.equals(SortingOption.NEW_PRODUCT.toString())){
            return productRepository
                    .findSeasonProductsOrderByCreateAtDesc(pageable, SeasonCategory.SUMMER)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 판매량순 조회일경우
        if(option.equals(SortingOption.BEST_SELLERS.toString())){
            return productRepository
                    .findSeasonProductsOrderBySalesVolumeDesc(pageable, SeasonCategory.SUMMER)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 인기순 조회일경우
        if(option.equals(SortingOption.POPULAR.toString())){
            return productRepository.findSeasonProductsOrderByLikeCountDesc(pageable, SeasonCategory.SUMMER)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }

        //위 조건에 걸리지 않으면 예외처리
        throw new CustomException(ProductExceptionCode.INVALID_OPTION);
    }


    public List<ProductCardViewModel> findWinterProductInformsByOption(ProductPaging productPaging, String option) {

        Pageable pageable = PageRequest.of(productPaging.page() - 1, productPaging.size());

        // 낮은가격순 조회일경우
        if(option.equals(SortingOption.LOW_PRICE.toString())){
            return productRepository
                    .findSeasonProductsOrderByPriceAsc(pageable, SeasonCategory.WINTER)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 신상품순 조회일 경우
        if(option.equals(SortingOption.NEW_PRODUCT.toString())){
            return productRepository
                    .findSeasonProductsOrderByCreateAtDesc(pageable, SeasonCategory.WINTER)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 판매량순 조회일경우
        if(option.equals(SortingOption.BEST_SELLERS.toString())){
            return productRepository
                    .findSeasonProductsOrderBySalesVolumeDesc(pageable, SeasonCategory.WINTER)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }
        // 인기순 조회일경우
        if(option.equals(SortingOption.POPULAR.toString())){
            return productRepository.findSeasonProductsOrderByLikeCountDesc(pageable, SeasonCategory.WINTER)
                    .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));
        }

        //위 조건에 걸리지 않으면 예외처리
        throw new CustomException(ProductExceptionCode.INVALID_OPTION);
    }


    // 여름 카테고리 클릭시 조회 메서드 (인기순 조회)
    public List<ProductCardViewModel> findSummerProductInforms(ProductPaging productPaging) {
        Pageable pageable = PageRequest.of(productPaging.page() - 1, productPaging.size());

        List<ProductCardViewModel> summerProductsOrderByLikeCountDesc = productRepository
                .findSeasonProductsOrderByLikeCountDesc(pageable, SeasonCategory.SUMMER)
                .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));

        return summerProductsOrderByLikeCountDesc;
    }

    // 겨울 카테고리 클릭시 조회 메서드 (인기순 조회)
    public List<ProductCardViewModel> findWinterProductInforms(ProductPaging productPaging) {
        Pageable pageable = PageRequest.of(productPaging.page() - 1, productPaging.size());

        List<ProductCardViewModel> productCardViewModels = productRepository
                .findSeasonProductsOrderByLikeCountDesc(pageable, SeasonCategory.WINTER)
                .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_EMPTY));

        return productCardViewModels;
    }
*/


}
