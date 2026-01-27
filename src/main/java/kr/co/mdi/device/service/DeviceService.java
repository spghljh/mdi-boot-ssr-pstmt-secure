package kr.co.mdi.device.service;

import java.util.List;
import java.util.Map;

import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.device.dto.ManfStatDTO;

public interface DeviceService {

	int getTotalDeviceCount();

	List<DeviceDTO> getDeviceList();

	DeviceDTO getDeviceById(Integer deviceId);
	
	// ----
	
	List<DeviceDTO> getDevicesByCpuName(String cpuName);
	
	// ----
	
	Map<String, Integer> getDeviceCountByBrand(String cpuName);
	
	//
	
	List<DeviceDTO> getDeviceListByName(String nameDevice);
	
	List<DeviceDTO> getHotDeviceList();

	List<DeviceDTO> getDeviceListByManufacturer(String manfDevice);
	
	//
	
	List<DeviceDTO> getDeviceListByCpuId(int idCpu);
	
	List<ManfStatDTO> getDeviceManufacturerDistribution();
	
	List<DeviceDTO> getDeviceListByType(String typeCode);
	
	List<DeviceDTO> getDeviceListByReleaseYear(int releaseYear);
	
	//
	
	List<DeviceDTO> getDeviceListByCpuAndManf(int idCpu, String manfDevice);
}
