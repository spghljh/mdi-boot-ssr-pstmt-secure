package kr.co.mdi.device.dao;

import java.util.List;
import java.util.Map;

import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.device.dto.ManfStatDTO;

public interface DeviceDao {

	int selectTotalDeviceCount();

	List<DeviceDTO> selectAllDevices();

	DeviceDTO selectDeviceById(Integer deviceId);

	List<DeviceDTO> selectDevicesByCpuName(String cpuName);

	Map<String, Integer> selectDeviceCountByBrand(String cpuName);

	List<DeviceDTO> selectDeviceListByName(String nameDevice);

	List<DeviceDTO> selectHotDeviceList();

	List<DeviceDTO> selectDeviceListByManufacturer(String manfDevice);

	List<DeviceDTO> selectDeviceListByCpuId(int idCpu);

	List<ManfStatDTO> getDeviceManufacturerDistribution();

	List<DeviceDTO> selectDevicesByTypeCode(String typeCode);

	List<DeviceDTO> selectDeviceListByReleaseYear(int releaseYear);

	List<DeviceDTO> selectDeviceListByCpuAndManf(int idCpu, String manfDevice);

}
