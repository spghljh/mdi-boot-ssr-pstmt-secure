package kr.co.mdi.cpu.dto;

public class CpuManfDTO {
	private String cpuManfCode; // 예: "INTEL", "AMD"
	private String manfCpu; // 예: "intel", "amd"

	public String getCpuManfCode() {
		return cpuManfCode;
	}

	public void setCpuManfCode(String cpuManfCode) {
		this.cpuManfCode = cpuManfCode;
	}

	public String getManfCpu() {
		return manfCpu;
	}

	public void setManfCpu(String manfCpu) {
		this.manfCpu = manfCpu;
	}

}
