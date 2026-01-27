package kr.co.mdi.device.dto;

public class DeviceTypeDTO {
	private String deviceTypeCode; // 예: "LAPTOP", "MOBILE"
	private String typeDevice; // 예: "laptop", "mobile"

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

}
