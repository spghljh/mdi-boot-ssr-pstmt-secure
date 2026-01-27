// login-logic.js
function validateLoginForm() {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    if (username === "" && password === "") {
        alert("아이디와 비밀번호를 입력하세요.");
        return false;
    }
    if (username === "") {
        alert("아이디를 입력하세요.");
        return false;
    }
    if (password === "") {
        alert("비밀번호를 입력하세요.");
        return false;
    }
    return true; // 모두 입력되었으면 제출 진행
}
