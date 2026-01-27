package kr.co.mdi.member.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class MemberDTO {

	// Default
    private int idMember;           // 회원 고유 번호
    private String id;              // 로그인 ID
    private String pass;            // 비밀번호 (암호화됨)
    private String passConfirm;     // 비밀번호 확인 (폼에서만 사용)
    private String name;            // 사용자 이름
    private String email;           // 이메일 주소
    private LocalDateTime registDay;       // 가입일
    
    // Spring Security
    private String role;            // 권한 (USER, ADMIN 등)
    private String status;          // 계정 상태 (ACTIVE, SUSPENDED 등)
    private String emailVerified;   // 이메일 인증 여부 ('Y' 또는 'N')
    private int failCount;          // 로그인 실패 횟수
    private LocalDateTime lastLogin;       // 마지막 로그인 시간
    private LocalDateTime updatedAt;       // 정보 수정일
    private LocalDateTime deletedAt;       // 탈퇴 처리 시간

    private Set<Integer> cpuPreferenceIds;    // 관심 CPU ID 집합 → MemberCpuPreferenceDTO와 연결
    private Set<Integer> devicePreferenceIds; // 관심 디바이스 ID 집합 → MemberDevicePreferenceDTO와 연결

	public int getIdMember() {
		return idMember;
	}
	public void setIdMember(int idMember) {
		this.idMember = idMember;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getPassConfirm() {
		return passConfirm;
	}
	public void setPassConfirm(String passConfirm) {
		this.passConfirm = passConfirm;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDateTime getRegistDay() {
		return registDay;
	}
	public void setRegistDay(LocalDateTime registDay) {
		this.registDay = registDay;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(String emailVerified) {
		this.emailVerified = emailVerified;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public LocalDateTime getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}
	public Set<Integer> getCpuPreferenceIds() {
		return cpuPreferenceIds;
	}
	public void setCpuPreferenceIds(Set<Integer> cpuPreferenceIds) {
		this.cpuPreferenceIds = cpuPreferenceIds;
	}
	public Set<Integer> getDevicePreferenceIds() {
		return devicePreferenceIds;
	}
	public void setDevicePreferenceIds(Set<Integer> devicePreferenceIds) {
		this.devicePreferenceIds = devicePreferenceIds;
	}

	
    
    
	
}
