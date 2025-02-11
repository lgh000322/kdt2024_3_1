import React, { useState, useEffect } from "react";
import AdminPageLayout from "../layouts/AdminPageLayout";
import { useSelector } from "react-redux";
import { getMembers } from "../api/memberApi";

function AdminUserPage() {
  const loginSlice = useSelector((state) => state.loginSlice);
  const [formData, setFormData] = useState([]);
  const [searchTerm, setSearchTerm] = useState(""); // 검색어 상태
  const [loading, setLoading] = useState(true);
  const [searchField, setSearchField] = useState("name");
  const [error, setError] = useState(null); // 에러 상태
  const [page, setPage] = useState(0);

  // 회원 목록 가져오기 함수
  const fetchMembers = async (searchFieldValue = null, searchTermValue = null) => {
    setLoading(true);
    setError(null);
    try {
      // API 호출 시 검색 조건 전달
      const res = await getMembers(
        loginSlice.accessToken,
        page,
        10,
        searchFieldValue === "role" ? searchTermValue : null, // 역할 검색 조건 추가
        searchFieldValue === "email" ? searchTermValue : null, // 이메일 검색 조건
        searchFieldValue === "name" ? searchTermValue : null, // 이름 검색 조건
      );
      const sortedData = res.data.sort((a, b) => a.name.localeCompare(b.name));
      setFormData(sortedData);
    } catch (err) {
      console.error("회원 정보 조회 실패:", err);
      setError("회원 정보를 불러오는 데 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMembers();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div style={{ color: "red" }}>{error}</div>;

  return (
    <AdminPageLayout>
      <div>
        {/* 검색 필터 섹션 */}
        <div
          style={{
            padding: "80px",
            border: "1px solid #ddd",
            margin: "60px auto",
            width: "80%",
            backgroundColor: "#f7faff",
            borderRadius: "10px",
            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
          }}
        >
          <h2
            style={{
              marginBottom: "20px",
              color: "#333",
              fontSize: "20px",
              fontWeight: "bold",
            }}
          >
            회원 검색
          </h2>
          <div style={{ display: "flex", alignItems: "center", marginBottom: "15px" }}>
            {/* 검색어 입력 필드 */}
            <label
            htmlFor="searchTerm"
            style={{
              marginRight: "10px",
              fontWeight: "bold",
              color: "#555",
              minWidth: "80px",
            }}
            >
              검색어
            </label>
            <input
              type="text"
              placeholder="검색어 입력"
              value={searchTerm} // 검색어 상태와 연결
              onChange={(e) => setSearchTerm(e.target.value)} // 상태 업데이트
              style={{
                padding: "10px",
                border: "1px solid #ccc",
                borderRadius: "5px",
                width: "250px",
                marginRight: "10px",
              }}
            />
            {/* 드롭다운 박스 */}
            <select
              value={searchField}
              onChange={(e) => setSearchField(e.target.value)} // 드롭다운 선택 상태 업데이트
              style={{
                padding: "10px",
                borderRadius: "5px",
                border: "1px solid #ccc",
                backgroundColor: "#fff",
                cursor: "pointer",
              }}
            >
              <option value="name">이름</option>
              <option value="email">이메일</option>
              <option value="role">역할</option>
            </select>
          </div>
          <button
            onClick={() => fetchMembers(searchField, searchTerm)} // 검색 버튼 클릭 시 호출
            style={{
              backgroundColor: "#007BFF",
              color: "#fff",
              border: "none",
              padding: "10px 20px",
              borderRadius: "5px",
              cursor: "pointer",
              fontSize: "16px",
            }}
          >
            검색
          </button>
        </div>

        {/* 테이블 섹션 */}
        <div
          style={{
            padding: "20px",
            border: "1px solid #ddd",
            margin: "20px auto",
            width: "80%",
            backgroundColor: "#fff",
            borderRadius: "10px",
            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
          }}
        >
          <h2
            style={{
              marginBottom: "20px",
              color: "#333",
              fontSize: "20px",
              fontWeight: "bold",
            }}
          >
            회원 목록
          </h2>
          <div style={{ maxHeight: "400px", overflowY: "auto" }}>
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead
                style={{
                  position: "sticky",
                  top: 0,
                  backgroundColor: "#f7faff",
                  zIndex: 1,
                }}
              >
                <tr>
                  {["번호", "이름", "이메일", "성별", "전화번호", "역할"].map(
                    (header, index) => (
                      <th
                        key={index}
                        style={{
                          borderBottom: "2px solid #ddd",
                          padding: "10px",
                          fontWeight: "bold",
                          color: "#555",
                        }}
                      >
                        {header}
                      </th>
                    )
                  )}
                </tr>
              </thead>
              <tbody>
                {formData.map((member, index) => (
                  <tr key={member.id}>
                    {/* 번호는 index + 1로 설정 */}
                    <td style={{ padding: "10px", textAlign: "center" }}>
                      {index + 1}
                    </td>
                    <td style={{ padding: "10px", textAlign: "center" }}>
                      {member.name}
                    </td>
                    <td style={{ padding: "10px", textAlign: "center" }}>
                      {member.email}
                    </td>
                    <td style={{ padding: "10px", textAlign: "center" }}>
                      {member.gender}
                    </td>
                    <td style={{ padding: "10px", textAlign: "center" }}>
                      {member.tel}
                    </td>
                    <td style={{ padding: "10px", textAlign: "center" }}>
                      {member.role}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </AdminPageLayout>
  );
}

export default AdminUserPage;
