package kr.co.mdi.cpu.dto;

public class CpuTypeDTO {
	private String cpuTypeCode; // 예: "LAPTOP", "MOBILE_AP"
	private String typeCpu; // 예: "laptop cpu", "mobile ap"

	public String getCpuTypeCode() {
		return cpuTypeCode;
	}

	public void setCpuTypeCode(String cpuTypeCode) {
		this.cpuTypeCode = cpuTypeCode;
	}

	public String getTypeCpu() {
		return typeCpu;
	}

	public void setTypeCpu(String typeCpu) {
		this.typeCpu = typeCpu;
	}

}
