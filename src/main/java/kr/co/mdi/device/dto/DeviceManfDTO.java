package kr.co.mdi.device.dto;

public class DeviceManfDTO {
	private String deviceManfCode; // 예: "HP", "ASUS", "APPLE"
	private String manfDevice; // 예: "hp", "asus", "apple"

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

}
