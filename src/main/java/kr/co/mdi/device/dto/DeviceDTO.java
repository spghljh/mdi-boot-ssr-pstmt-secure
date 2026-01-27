package kr.co.mdi.device.dto;

public class DeviceDTO {

	private Integer idDevice; // mdl.id_device
	private String nameDevice; // mdl.name_device
	private String lineupDevice; // mdl.lineup_device
	private Integer releaseDevice; // mdl.release_device
	private Float weightDevice; // mdl.weight_device
	private Integer choiceDevice; // mdl.choice_device

	private String deviceTypeCode; // mdl.device_type_code (외래 키)
	private String typeDevice; // device_type.type_device (표시용 이름)

	private String deviceManfCode; // mdl.device_manf_code (외래 키)
	private String manfDevice; // device_manf_brand.manf_device (표시용 이름)

	private Integer idCpu; // mdl.id_cpu (외래 키 → mcl.id_cpu)
	private String cpuDevice; // mcl.name_cpu (표시용 CPU 이름)
	private String manfCpu; // mcl.manf_cpu (CPU 제조사 이름)
	
	public Integer getIdDevice() {
		return idDevice;
	}
	public void setIdDevice(Integer idDevice) {
		this.idDevice = idDevice;
	}
	public String getNameDevice() {
		return nameDevice;
	}
	public void setNameDevice(String nameDevice) {
		this.nameDevice = nameDevice;
	}
	public String getLineupDevice() {
		return lineupDevice;
	}
	public void setLineupDevice(String lineupDevice) {
		this.lineupDevice = lineupDevice;
	}
	public Integer getReleaseDevice() {
		return releaseDevice;
	}
	public void setReleaseDevice(Integer releaseDevice) {
		this.releaseDevice = releaseDevice;
	}
	public Float getWeightDevice() {
		return weightDevice;
	}
	public void setWeightDevice(Float weightDevice) {
		this.weightDevice = weightDevice;
	}
	public Integer getChoiceDevice() {
		return choiceDevice;
	}
	public void setChoiceDevice(Integer choiceDevice) {
		this.choiceDevice = choiceDevice;
	}
	public String getDeviceTypeCode() {
		return deviceTypeCode;
	}
	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}
	public String getTypeDevice() {
		return typeDevice;
	}
	public void setTypeDevice(String typeDevice) {
		this.typeDevice = typeDevice;
	}
	public String getDeviceManfCode() {
		return deviceManfCode;
	}
	public void setDeviceManfCode(String deviceManfCode) {
		this.deviceManfCode = deviceManfCode;
	}
	public String getManfDevice() {
		return manfDevice;
	}
	public void setManfDevice(String manfDevice) {
		this.manfDevice = manfDevice;
	}
	public Integer getIdCpu() {
		return idCpu;
	}
	public void setIdCpu(Integer idCpu) {
		this.idCpu = idCpu;
	}
	public String getCpuDevice() {
		return cpuDevice;
	}
	public void setCpuDevice(String cpuDevice) {
		this.cpuDevice = cpuDevice;
	}
	public String getManfCpu() {
		return manfCpu;
	}
	public void setManfCpu(String manfCpu) {
		this.manfCpu = manfCpu;
	}
	
	

}
