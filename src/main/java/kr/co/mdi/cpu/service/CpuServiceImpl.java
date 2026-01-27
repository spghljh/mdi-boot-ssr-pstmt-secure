package kr.co.mdi.cpu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mdi.cpu.dao.CpuDao;
import kr.co.mdi.cpu.dto.CoreStatDTO;
import kr.co.mdi.cpu.dto.CpuDTO;

@Service
public class CpuServiceImpl implements CpuService {

	private final CpuDao cpuDAO;

	public CpuServiceImpl(CpuDao cpuDAO) {
		this.cpuDAO = cpuDAO;
	}

	@Override
	public int getTotalCpuCount() {
		return cpuDAO.selectTotalCpuCount();
	}

//	@Override
//	public List<CpuDTO> getCpuList() {
//		return cpuDAO.selectAllCpus();
//	}
	
	@Override
	public List<CpuDTO> getCpuList() {
	    List<CpuDTO> list = cpuDAO.selectAllCpus();
	    for (CpuDTO cpu : list) {
	        System.out.println(cpu);
	    }
	    return list;
	}

//	@Override
//	public CpuDTO getCpuById(Integer cpuId, int appUserId) {
//	    return cpuDAO.selectCpuById(cpuId, appUserId);
//	}

	@Override
	public CpuDTO getCpuById(Integer cpuId) {
		return cpuDAO.selectCpuById(cpuId);
	}
	
	// ----------
	
	@Override
	public CpuDTO getCpuByName(String nameCpu) {
	    return cpuDAO.selectCpuByName(nameCpu);
	}

	// ----------
	
	@Override
	public List<CpuDTO> getCpuListByName(String nameCpu) {
	    return cpuDAO.selectCpuListByName(nameCpu);
	}
	
	@Override
	public List<CpuDTO> getHotCpuList() {
	    return cpuDAO.selectHotCpuList();
	}

	@Override
	public List<CpuDTO> getCpuListByManufacturer(String manfCpu) {
	    return cpuDAO.selectCpuListByManufacturer(manfCpu);
	}

	@Override
	public List<CpuDTO> getCpuListByCore(int coreCpu) {
	    return cpuDAO.selectCpuListByCore(coreCpu);
	}

	@Override
	public List<CpuDTO> getCpuListByThread(int threadCpu) {
	    return cpuDAO.selectCpuListByThread(threadCpu);
	}

	@Override
	public List<CpuDTO> getCpuListByRelease(int releaseCpu) {
	    return cpuDAO.selectCpuListByRelease(releaseCpu);
	}
	
	@Override
	public List<CoreStatDTO> getCoreCpuDistribution() {
	    return cpuDAO.getCoreCpuDistribution();
	}



}
