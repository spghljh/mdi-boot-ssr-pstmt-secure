package kr.co.mdi.cpu.dto;

public class CpuDTO {
	private Integer idCpu; // mcl.id_cpu
	private String nameCpu; // mcl.name_cpu
	private Integer releaseCpu; // mcl.release_cpu
	private Integer coreCpu; // mcl.core_cpu
	private Integer threadCpu; // mcl.thread_cpu
	private Float maxghzCpu; // mcl.maxghz_cpu
	private Float minghzCpu; // mcl.minghz_cpu
	private Integer choiceCpu; // mcl.choice_cpu

	private String cpuTypeCode; // mcl.cpu_type_code (외래 키)
	private String typeCpu; // cpu_type.type_cpu (표시용 이름)

	private String cpuManfCode; // mcl.cpu_manf_code (외래 키)
	private String manfCpu; // cpu_manf_brand.manf_cpu (해당 cpu의 제조사 정보)
	public Integer getIdCpu() {
		return idCpu;
	}
	public void setIdCpu(Integer idCpu) {
		this.idCpu = idCpu;
	}
	public String getNameCpu() {
		return nameCpu;
	}
	public void setNameCpu(String nameCpu) {
		this.nameCpu = nameCpu;
	}
	public Integer getReleaseCpu() {
		return releaseCpu;
	}
	public void setReleaseCpu(Integer releaseCpu) {
		this.releaseCpu = releaseCpu;
	}
	public Integer getCoreCpu() {
		return coreCpu;
	}
	public void setCoreCpu(Integer coreCpu) {
		this.coreCpu = coreCpu;
	}
	public Integer getThreadCpu() {
		return threadCpu;
	}
	public void setThreadCpu(Integer threadCpu) {
		this.threadCpu = threadCpu;
	}
	public Float getMaxghzCpu() {
		return maxghzCpu;
	}
	public void setMaxghzCpu(Float maxghzCpu) {
		this.maxghzCpu = maxghzCpu;
	}
	public Float getMinghzCpu() {
		return minghzCpu;
	}
	public void setMinghzCpu(Float minghzCpu) {
		this.minghzCpu = minghzCpu;
	}
	public Integer getChoiceCpu() {
		return choiceCpu;
	}
	public void setChoiceCpu(Integer choiceCpu) {
		this.choiceCpu = choiceCpu;
	}
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
	@Override
	public String toString() {
		return "CpuDTO [idCpu=" + idCpu + ", nameCpu=" + nameCpu + ", releaseCpu=" + releaseCpu + ", coreCpu=" + coreCpu
				+ ", threadCpu=" + threadCpu + ", maxghzCpu=" + maxghzCpu + ", minghzCpu=" + minghzCpu + ", choiceCpu="
				+ choiceCpu + ", cpuTypeCode=" + cpuTypeCode + ", typeCpu=" + typeCpu + ", cpuManfCode=" + cpuManfCode
				+ ", manfCpu=" + manfCpu + "]";
	}
	
	


}
