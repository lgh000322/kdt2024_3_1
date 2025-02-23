import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { logoutRefresh } from "../api/memberApi";
import useCustomMove from "../hook/useCustomMove";

const sellerMenuItems = [
  // {
  //   id: 1,
  //   title: "배송관리(판매자)",
  //   path: "/mypage/address-management",
  //   content: "배송관리 관련 콘텐츠",
  // },
  // {
  //   id: 2,
  //   title: "주문관리(판매자)",
  //   path: "/mypage/order-management",
  //   content: "주문관리 관련 콘텐츠",
  // },
  // {
  //   id: 3,
  //   title: "문의내역",
  //   path: "/mypage/inquiry",
  //   content: "문의내역 관련 콘텐츠",
  // },
  {
    id: 4,
    title: "찜리스트",
    path: "/mypage/wishlist",
    content: "찜리스트 관련 콘텐츠",
  },
  {
    id: 5,
    title: "주문목록 / 배송조회",
    path: "/mypage/order-list",
    content: "주문목록 및 배송조회 관련 콘텐츠",
  },
  {
    id: 6,
    title: "배송지 관리",
    path: "/mypage/shipping-address",
    content: "배송지 관리 관련 콘텐츠",
  },
  {
    id: 7,
    title: "상품 등록",
    path: "/product/registration",
    content: "상품 업로드",
  },
  {
    id: 8,
    title: "등록한 상품 조회",
    path: "/seller/products",
    content: "내가 등록한 상품 조회",
  },
];

const consumerMenuItems = [
  {
    id: 1,
    title: "주문목록 / 배송조회",
    path: "/mypage/order-list",
    content: "주문목록 및 배송조회 관련 콘텐츠",
  },
  {
    id: 2,
    title: "배송지 관리",
    path: "/mypage/shipping-address",
    content: "배송지 관리 관련 콘텐츠",
  },
  // {
  //   id: 3,
  //   title: "문의내역",
  //   path: "/mypage/inquiry",
  //   content: "문의내역 관련 콘텐츠",
  // },
  {
    id: 4,
    title: "찜리스트",
    path: "/mypage/wishlist",
    content: "찜리스트 관련 콘텐츠",
  },
  {
    id: 5,
    title: "판매자 등록 신청",
    path: "/mypage/seller-registration",
    content: "판매자 등록 신청 관련 콘텐츠",
  },
];

const managerMenuItems = [
  {
    id: 1,
    title: "회원관리",
    path: "/admin_user",
    content: "회원관리 관련 콘텐츠",
  },
  {
    id: 2,
    title: "판매자 목록",
    path: "/admin_seller",
    content: "판매자 목록 관련 콘텐츠",
  },
  {
    id: 3,
    title: "판매자 승인",
    path: "/admin_accept",
    content: "판매자 승인 관련 콘텐츠",
  },
  {
    id: 4,
    title: "배너 관리",
    path: "/admin_banner",
    content: "배너관리 관련 콘텐츠",
  },
  {
    id: 5,
    title: "찜리스트",
    path: "/mypage/wishlist",
    content: "찜리스트 관련 콘텐츠",
  },
];

const Sidebar = ({ onMenuClick }) => {
  const { doLogout, moveToLoginPage } = useCustomMove();
  const loginState = useSelector((state) => state.loginSlice);
  const [menuItems, setMenuItems] = useState([]);

  useEffect(() => {
    let loginStateJson = JSON.stringify(loginState.role);
    const jsonObject = JSON.parse(loginStateJson);
    const roleValue = jsonObject.role[0];

    if (roleValue === "ROLE_SELLER" || roleValue === "SELLER") {
      setMenuItems(sellerMenuItems);
    } else if (roleValue === "ROLE_USER" || roleValue === "USER") {
      setMenuItems(consumerMenuItems);
    } else {
      setMenuItems(managerMenuItems);
    }
  }, []);

  const logoutClick = () => {
    logoutRefresh().then((res) => {
      if (res.code === 200) {
        doLogout();
        alert("로그아웃에 성공했습니다.");
        moveToLoginPage();
      } else {
        alert("로그아웃에 실패했습니다.");
      }
    });
  };

  return (
    <div className="w-64 min-h-screen bg-gray-50">
      {/* 마이페이지 텍스트 */}
      <div className="p-4 text-black font-semibold">마이페이지</div>

      {/* 구분선 */}
      <hr className="border-gray-300" />

      {/* 사이드바 메뉴 */}
      <nav className="p-4">
        {menuItems.map((item) => (
          <div key={item.id}>
            <Link to={item.path}>
              <button
                className="w-full text-left px-4 py-2.5 text-gray-700
                          hover:bg-white hover:text-blue-600
                          rounded-lg transition-colors duration-200"
                onClick={() => onMenuClick(item)}
              >
                {item.title}
              </button>
            </Link>
          </div>
        ))}
      </nav>

      {/* 로그인/로그아웃 구분선 */}
      <div className="p-4 border-t border-gray-200">
        <button
          className="w-full text-left px-4 py-2.5 text-gray-700
                          hover:bg-white hover:text-blue-600 
                          rounded-lg transition-colors duration-200"
          onClick={logoutClick}
        >
          로그아웃
        </button>
      </div>
    </div>
  );
};

export default Sidebar;
