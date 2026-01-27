// member-register.js
function validateRegisterForm() {
    const id = document.getElementById("id").value.trim();
    const pass = document.getElementById("pass").value.trim();
    const passConfirm = document.getElementById("passConfirm").value.trim();
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();

    if (!id || !pass || !passConfirm || !name || !email) {
        alert("빈칸 없이 입력해 주세요!");
        return false; // 제출 막음
    }
    if (pass !== passConfirm) {
        alert("입력하신 비밀번호가 일치하지 않습니다!");
        return false; // 제출 막음
    }
    return true; // 검증 통과 → 서버 제출 허용
}
