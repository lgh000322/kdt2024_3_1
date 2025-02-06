import { createSlice } from "@reduxjs/toolkit";
import { getCookies, removeCookie, setCookie } from "../utils/cookieUtils";
import { getRoleFromAccessToken } from "../utils/jwtUtils";

const initState = {
  accessToken: "",
  role: "",
};

const loadState = () => {
  const accessToken = getCookies("accessToken");

  if (!accessToken) return initState;

  // JWT의 Payload 부분 가져오기
  const base64Url = accessToken.split(".")[1];

  if (!base64Url) return initState;

  // Base64 디코딩
  try {
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const decodedPayload = JSON.parse(atob(base64));

     // role 정보 추출
     const role = decodedPayload.role;

    return { accessToken, role };
  } catch (error) {
    console.error("Failed to decode JWT:", error);
    return initState;
  }
};

const loginSlice = createSlice({
  name: "loginSlice",
  initialState: loadState() || initState,
  reducers: {
    login: (state, action) => {
      state.accessToken = action.payload.accessToken;
      state.role = action.payload.role;
    },
    logout: (state) => {
      removeCookie("accessToken");
      state.accessToken = ""; // initState로 상태 재설정
      state.role = "";
    },
    refresh: (state, action) => {
      state.accessToken = action.payload.accessToken;
      state.role = Array.isArray(action.payload.role)
        ? action.payload.role
        : [action.payload.role]; // role이 배열인지 확인 후 배열로 설정
    },
  },
});

export const { login, logout, refresh } = loginSlice.actions;
export default loginSlice.reducer;
