package kr.co.mdi.member.dto;

public class MemberCpuPreferenceDTO {

	private String id_member; // 회원 PK
	private Integer cpuId; // 찜한 CPU ID
	
	public String getId_member() {
		return id_member;
	}
	public void setId_member(String id_member) {
		this.id_member = id_member;
	}
	public Integer getCpuId() {
		return cpuId;
	}
	public void setCpuId(Integer cpuId) {
		this.cpuId = cpuId;
	}

}
