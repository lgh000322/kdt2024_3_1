import React, { useEffect, useState } from "react";
import BasicLayout from "../layouts/BasicLayout";
import ProductCardComponent from "../components/ProductCardComponent";
import SubProductLayout from "../layouts/SubProductLayout";
import { getProductList } from "../api/productApi";

function SummerPage() {
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false); // 로딩 상태 (중복 API 요청 방지)
  const [noMoreProducts, setNoMoreProducts] = useState(false); // 데이터가 없을 때 호출하는 것을 방지

  const handleScroll = () => {
    const scrollableHeight = document.documentElement.scrollHeight;
    const currentScroll = window.innerHeight + window.scrollY;

    // 페이지 하단에 도달했을 때 더 많은 상품을 불러오기
    if (currentScroll >= scrollableHeight - 100 && !loading) {
      setPage((prevPage) => prevPage + 1); // 페이지 번호 증가
    }
  };

  useEffect(() => {
    if (noMoreProducts || loading) return; // 로딩 중일 경우 API 요청 방지

    setLoading(true);
    getProductList(page, 10, "SUMMER", null, null, null, null)
      .then((res) => {
        const data = res.data;
        if (data.length === 0) {
          setNoMoreProducts(true);
        }
        setProducts((prevProducts) => [...prevProducts, ...data]); // 새로운 상품을 기존 상품 리스트에 추가
      })
      .finally(() => {
        setLoading(false); // 로딩 완료 후 상태 리셋
      });
  }, [page]);

  // 스크롤 이벤트를 감지하여 페이지 하단에 도달하면 더 많은 상품을 로드
  useEffect(() => {
    // 더 이상 가져올 상품이 없으면 실행하지 않음
    if (noMoreProducts) return;

    window.addEventListener("scroll", handleScroll);

    // 컴포넌트 언마운트 시 이벤트 리스너 제거
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  return (
    <SubProductLayout>
      {/* 상품 그리드 섹션 */}
      <div className="max-w-7xl mx-auto px-4 pt-12">
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {products.map((product) => (
            <div key={product.productId} className="w-full">
              <ProductCardComponent
                id={product.productId}
                title={product.productName}
                price={product.price}
                image={product.imgUrl}
              />
            </div>
          ))}
        </div>
      </div>
    </SubProductLayout>
  );
}

export default SummerPage;
