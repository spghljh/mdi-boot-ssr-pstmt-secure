package kr.co.mdi.cpu.service;

import java.util.List;

import kr.co.mdi.cpu.dto.CoreStatDTO;
import kr.co.mdi.cpu.dto.CpuDTO;

public interface CpuService {
	int getTotalCpuCount();

	List<CpuDTO> getCpuList();

	CpuDTO getCpuById(Integer cpuId);
	
	CpuDTO getCpuByName(String nameCpu);

	// -------
	
	List<CpuDTO> getCpuListByName(String nameCpu);
	
	List<CpuDTO> getHotCpuList();
	
	//
	
	List<CpuDTO> getCpuListByManufacturer(String manfCpu);

	List<CpuDTO> getCpuListByCore(int coreCpu);
	List<CpuDTO> getCpuListByThread(int threadCpu);
	List<CpuDTO> getCpuListByRelease(int releaseCpu);

	List<CoreStatDTO> getCoreCpuDistribution();

}
