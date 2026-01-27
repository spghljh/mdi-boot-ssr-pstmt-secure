package kr.co.mdi.device.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mdi.device.dao.DeviceDao;
import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.device.dto.ManfStatDTO;

@Service
public class DeviceServiceImpl implements DeviceService {

	private final DeviceDao deviceDAO;

	public DeviceServiceImpl(DeviceDao deviceDAO) {
		this.deviceDAO = deviceDAO;
	}

	@Override
	public int getTotalDeviceCount() {
		return deviceDAO.selectTotalDeviceCount();
	}

	@Override
	public List<DeviceDTO> getDeviceList() {
		return deviceDAO.selectAllDevices();
	}

	@Override
	public DeviceDTO getDeviceById(Integer deviceId) {
		return deviceDAO.selectDeviceById(deviceId);
	}
	
	// ----
	
	@Override
	public List<DeviceDTO> getDevicesByCpuName(String cpuName) {
	    return deviceDAO.selectDevicesByCpuName(cpuName);
	}

	// ----
	
	@Override
	public Map<String, Integer> getDeviceCountByBrand(String cpuName) {
	    return deviceDAO.selectDeviceCountByBrand(cpuName);
	}
	
	@Override
	public List<DeviceDTO> getDeviceListByName(String nameDevice) {
	    return deviceDAO.selectDeviceListByName(nameDevice);
	}
	
	@Override
	public List<DeviceDTO> getHotDeviceList() {
	    return deviceDAO.selectHotDeviceList();
	}

	@Override
	public List<DeviceDTO> getDeviceListByManufacturer(String manfDevice) {
	    return deviceDAO.selectDeviceListByManufacturer(manfDevice);
	}

	//
	
	@Override
	public List<DeviceDTO> getDeviceListByCpuId(int idCpu) {
	    return deviceDAO.selectDeviceListByCpuId(idCpu);
	}
	
	@Override
	public List<ManfStatDTO> getDeviceManufacturerDistribution() {
	    return deviceDAO.getDeviceManufacturerDistribution();
	}
	
	@Override
	public List<DeviceDTO> getDeviceListByType(String typeCode) {
	    return deviceDAO.selectDevicesByTypeCode(typeCode);
	}

	@Override
	public List<DeviceDTO> getDeviceListByReleaseYear(int releaseYear) {
	    return deviceDAO.selectDeviceListByReleaseYear(releaseYear);
	}
	
	@Override
	public List<DeviceDTO> getDeviceListByCpuAndManf(int idCpu, String manfDevice) {
	    return deviceDAO.selectDeviceListByCpuAndManf(idCpu, manfDevice);
	}


}
