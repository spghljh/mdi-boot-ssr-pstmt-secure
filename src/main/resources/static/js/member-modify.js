// member-modify-logic.js
function check_input() {
    const pass = document.getElementById("pass").value.trim();
    const passConfirm = document.getElementById("passConfirm").value.trim();
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();

    if (!pass || !passConfirm || !name || !email) {
        alert("빈칸 없이 입력해 주세요!");
        return false;
    }
    if (pass !== passConfirm) {
        alert("입력하신 비밀번호가 일치하지 않습니다!");
        return false;
    }

    // 모든 검증 통과 → 폼 제출
    document.forms["member"].submit();
}
