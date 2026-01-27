package kr.co.mdi.member.dto;

public class MemberDevicePreferenceDTO {

	private String id_member; // 회원 PK
	private Integer deviceId; // 찜한 디바이스 ID
	
	public String getId_member() {
		return id_member;
	}
	public void setId_member(String id_member) {
		this.id_member = id_member;
	}
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

}
