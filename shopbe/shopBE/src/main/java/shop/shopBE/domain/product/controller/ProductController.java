package shop.shopBE.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.shopBE.domain.product.entity.enums.PersonCategory;
import shop.shopBE.domain.product.entity.enums.SeasonCategory;
import shop.shopBE.domain.product.request.ProductPaging;
import shop.shopBE.domain.product.response.ProductCardViewModel;
import shop.shopBE.domain.product.response.ProductInformsModelView;
import shop.shopBE.domain.product.service.ProductService;
import shop.shopBE.global.response.ResponseFormat;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "상품", description = "상품 관련 API")
public class ProductController {
    private final ProductService productService;

    // 메인페이지 상품조회 likeCount로 조회
    @GetMapping("/main")
    @Operation(summary = "메인페이지 상품 조회", description = "메인페이지에 인기(좋아요)순으로 상품을 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getMainPageProductsView(@RequestBody @Valid ProductPaging productPaging){
        List<ProductCardViewModel> mainPageCardViews = productService.findMainPageCardViews(productPaging);
        return ResponseEntity.ok().body(ResponseFormat.of("메인 페이지 상품 조회 성공", mainPageCardViews));
    }


    // 모든 시즌 상품 조회
    @GetMapping("/season/all")
    @Operation(summary = "모든시즌 상품 조회", description = "모든시즌의 상품을 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getAllSeasonProductsView(@RequestBody @Valid ProductPaging productPaging) {
        List<ProductCardViewModel> summerProductCardViewModels = productService.findSeasonProductInforms(productPaging, null);
        return ResponseEntity.ok().body(ResponseFormat.of("모든시즌 상품 조회 성공 (인기순)", summerProductCardViewModels));
    }

    // 모든 상품 조회 - 옵션 선택(인기순, 판매순, 낮은 가격순, 신상품(입고)순)
    @GetMapping("/season/all/{option}")
    @Operation(summary = "모든시즌 상품 옵션별 조회", description = "모든시즌의 상품을 옵션별로 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getAllSeasonProductViewsByOption(@RequestBody @Valid ProductPaging productPaging,
                                                                                                    @PathVariable("option") String option) {
        List<ProductCardViewModel> summerProductCardViewModelsByOption = productService.findSeasonProductInformsByOption(productPaging, null, option);
        return ResponseEntity.ok().body(ResponseFormat.of("모든시즌 상품 옵션별 조회 성공", summerProductCardViewModelsByOption));
    }


    // 여름 상품 조회.
    @GetMapping("/season/summer")
    @Operation(summary = "여름시즌 상품 조회", description = "여름시즌의 상품을 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getSummerProductsView(@RequestBody @Valid ProductPaging productPaging) {
        List<ProductCardViewModel> summerProductCardViewModels = productService.findSeasonProductInforms(productPaging, SeasonCategory.SUMMER);
        return ResponseEntity.ok().body(ResponseFormat.of("여름 상품 조회 성공 (인기순)", summerProductCardViewModels));
    }

    //여름 상품 조회 - 옵션 선택(인기순, 판매순, 낮은 가격순, 신상품(입고)순)
    @GetMapping("/season/summer/{option}")
    @Operation(summary = "여름시즌 상품 옵션별 조회", description = "여름시즌의 상품을 옵션별로 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getSummerProductViewsByOption(@RequestBody @Valid ProductPaging productPaging,
                                                                                                    @PathVariable("option") String option) {
        List<ProductCardViewModel> summerProductCardViewModelsByOption = productService.findSeasonProductInformsByOption(productPaging, SeasonCategory.SUMMER, option);
        return ResponseEntity.ok().body(ResponseFormat.of("여름시즌 상품 옵션별 조회 성공", summerProductCardViewModelsByOption));
    }

    // 겨울 상품 조회
    @GetMapping("/season/winter")
    @Operation(summary = "겨울시즌 상품 조회", description = "겨울시즌의 상품을 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getWinterProductCardViewModels(@RequestBody @Valid ProductPaging productPaging) {
        List<ProductCardViewModel> winterProductCardViewModels = productService.findSeasonProductInforms(productPaging, SeasonCategory.WINTER);
        return ResponseEntity.ok().body(ResponseFormat.of("겨울 상품 조회 성공 (인기순)", winterProductCardViewModels));
    }

    //겨울 상품 조회 - 옵션 선택(인기순, 판매순, 낮은 가격순, 신상품(입고)순)
    @GetMapping("/season/winter/{option}")
    @Operation(summary = "겨울시즌 상품 옵션별 조회", description = "겨울시즌의 상품을 옵션별로 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getWinterProductViewsByOption(@RequestBody @Valid ProductPaging productPaging,
                                                                                                    @PathVariable("option") String option) {
        List<ProductCardViewModel>  winterProductCardViewModelsByOption = productService.findSeasonProductInformsByOption(productPaging, SeasonCategory.WINTER, option);
        return ResponseEntity.ok().body(ResponseFormat.of("겨울시즌 상품 옵션별 조회 성공", winterProductCardViewModelsByOption));
    }

    //남성(+ 남여공용) 프로덕트 카테고리별 조회
    @GetMapping("/men/{productCategory}")
    @Operation(summary = "남성상품 카테고리별 조회", description = "남성상품중 상품 카테고리별로 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getMenProductCardsByProductCategory(@RequestBody @Valid ProductPaging productPaging,
                                                                                                          @PathVariable("productCategory") String productCategory){
        List<ProductCardViewModel> menProductInformsByProductCategory = productService.findPersonProductInformsByProductCategory(productPaging, PersonCategory.MEN, productCategory);
        return ResponseEntity.ok().body(ResponseFormat.of("남성상품 카테고리별 조회 성공", menProductInformsByProductCategory));
    }

    //여성(+ 남여공용) 프로덕트 카테고리별 조회
    @GetMapping("/women/{productCategory}")
    @Operation(summary = "여성상품 카테고리별 조회", description = "여성상품중 상품 카테고리별로 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getWomenProductCardsByProductCategory(@RequestBody @Valid ProductPaging productPaging,
                                                                                                          @PathVariable("productCategory") String productCategory){
        List<ProductCardViewModel> womenProductInformsByProductCategory = productService.findPersonProductInformsByProductCategory(productPaging, PersonCategory.WOMEN, productCategory);
        return ResponseEntity.ok().body(ResponseFormat.of("여성상품 카테고리별 조회 성공", womenProductInformsByProductCategory));
    }

    // 아동(아동만) 프로덕트 카테고리별 조회
    @GetMapping("/children/{productCategory}")
    @Operation(summary = "아동상품 카테고리별 조회", description = "아동상품중 상품 카테고리별로 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getChildrenProductCardsByProductCategory(@RequestBody @Valid ProductPaging productPaging,
                                                                                                            @PathVariable("productCategory") String productCategory){
        List<ProductCardViewModel> childrenProductInformsByProductCategory = productService.findPersonProductInformsByProductCategory(productPaging, PersonCategory.CHILDREN, productCategory);
        return ResponseEntity.ok().body(ResponseFormat.of("아동상품 카테고리별 조회 성공", childrenProductInformsByProductCategory));
    }

    // A. 사람(남, 여, 아동)아래 상품카테고리(슬리퍼, 부츠, 운동화 등등)아래 정렬조건(낮은가격, 인기순, 판매순 등) 조회

    // A - 1. 남자(+ 남여공용) 아래 상품카테고리(슬리퍼, 부츠, 운동화 등등)아래 정렬조건(낮은가격, 인기순, 판매순 등) 조회
    @GetMapping("/men/{productCategory}/{option}")
    @Operation(summary = "남성상품 옵션별 조회", description = "남성상품중 상품 카테고리별 상품아래 옵션별로 정렬된 상품을 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getMenProductsByOption(@RequestBody @Valid ProductPaging productPaging,
                                                                                             @PathVariable("productCategory") String productCategory,
                                                                                             @PathVariable("option") String option) {
        List<ProductCardViewModel> menProductInformsByOption = productService
                .findPersonProductInformsByOption(productPaging, PersonCategory.MEN, productCategory, option);

        return ResponseEntity.ok().body(ResponseFormat.of("남성상품 옵션별 조회 성공", menProductInformsByOption));
    }

    // A - 2. 여성(+ 남여공용) 아래 상품카테고리(슬리퍼, 부츠, 운동화 등등)아래 정렬조건(낮은가격, 인기순, 판매순 등) 조회
    @GetMapping("/women/{productCategory}/{option}")
    @Operation(summary = "여성상품 옵션별 조회", description = "여성상품중 상품 카테고리별 상품아래 옵션별로 정렬된 상품을 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getWomenProductsByOption(@RequestBody @Valid ProductPaging productPaging,
                                                                                             @PathVariable("productCategory") String productCategory,
                                                                                             @PathVariable("option") String option) {
        List<ProductCardViewModel> menProductInformsByOption = productService
                .findPersonProductInformsByOption(productPaging, PersonCategory.WOMEN, productCategory, option);

        return ResponseEntity.ok().body(ResponseFormat.of("여성상품 옵션별 조회 성공", menProductInformsByOption));
    }

    // A - 3. 아동 (아동만) 아래 상품카테고리(슬리퍼, 부츠, 운동화 등등)아래 정렬조건(낮은가격, 인기순, 판매순 등) 조회
    @GetMapping("/children/{productCategory}/{option}")
    @Operation(summary = "아동상품 옵션별 조회", description = "아동상품중 상품 카테고리별 상품아래 옵션별로 정렬된 상품을 보여준다.")
    public ResponseEntity<ResponseFormat<List<ProductCardViewModel>>> getChildrenProductsByOption(@RequestBody @Valid ProductPaging productPaging,
                                                                                               @PathVariable("productCategory") String productCategory,
                                                                                               @PathVariable("option") String option) {
        List<ProductCardViewModel> menProductInformsByOption = productService
                .findPersonProductInformsByOption(productPaging, PersonCategory.CHILDREN, productCategory, option);

        return ResponseEntity.ok().body(ResponseFormat.of("아동상품 옵션별 조회 성공", menProductInformsByOption));
    }

    // 상품 상세 조회 - 상품아이디를 통해 상품의 세부사항를 보여줌
    @GetMapping("/details/{productId}")
    @Operation(summary = "상품의 세부사항 조회", description = "상품의 번호를 통해 상품에 상세 설명을 보여준다")
    public ResponseEntity<ResponseFormat<ProductInformsModelView>> findProductDetailsByProductId(@PathVariable("productId") Long productId){

        ProductInformsModelView productInformsModelView = productService.findProductDetailsByProductId(productId);
        return ResponseEntity.ok().body(ResponseFormat.of("상품 상세정보 조회 성공", productInformsModelView));
    }


    // 상품 등록


    // 상품 제거



}
