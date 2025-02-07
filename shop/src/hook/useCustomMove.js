import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { login, logout } from "../slice/loginSlice";

const useCustomMove = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const doLogin = (accessToken, role) => {
    dispatch(login({ accessToken: accessToken, role: role }));
  };

  const doLogout = () => {
    dispatch(logout());
  };

  const moveToProduct = () => {
    navigate({ pathname: "../product" });
  };

  const moveToProductAbs = () => {
    navigate({ pathname: "/product" });
  };

  const moveToAuthenticated = () => {
    navigate({ pathname: "/login/authenticate" });
  };


  const moveToLoginPage = () => {
    navigate({ pathname: "/login" });
  };
  return {
    moveToProduct,
    moveToProductAbs,
    moveToAuthenticated,
    doLogin,
    doLogout,
    moveToLoginPage,
  };
};

export default useCustomMove;
