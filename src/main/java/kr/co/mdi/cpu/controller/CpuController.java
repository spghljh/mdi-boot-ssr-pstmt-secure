package kr.co.mdi.cpu.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.mdi.cpu.dto.CoreStatDTO;
import kr.co.mdi.cpu.dto.CpuDTO;
import kr.co.mdi.cpu.service.CpuService;
import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.device.service.DeviceService;

@Controller
public class CpuController {

//	Spring Framework 4.3 이후부터 단일 생성자에 Autowired 어노테이션 생략 가능.

	// -----------------------------------------------

//	1) 필드 주입 : 기본 생성자로 new 후 리플렉션으로 private 필드 주입	
//	Mock 객체 주입 불편
//	IoC 컨테이너 필요
//	ApplicationContext를 통한 통합테스트 적합
//	final 불가
//	
//	@Autowired
//	private CpuService cpuService;
//
//	@Autowired
//	private DeviceService deviceService;

	// -----------------------------------------------

//	2) 생성자 주입 : 리플렉션으로 생성자 호출 및 빈 인스턴스를 파라미터로 전달
//	Mock 객체 주입 용이
//	직접 객체 생성 가능
//	Mock 객체를 통한 단위테스트 적합
//	final 가능

	private final CpuService cpuService;
	private final DeviceService deviceService;

	public CpuController(CpuService cpuService, DeviceService deviceService) {
		this.cpuService = cpuService;
		this.deviceService = deviceService;
	}

	/**
	 * 전체 프로세서(P1)에 대한 통계와 목록
	 * 
	 * @param model 전체 프로세서 모델
	 * @return cpu-list
	 */
	@GetMapping("/cpus")
	public String cpuList(Model model) {
		List<CpuDTO> cpuList = cpuService.getCpuList();
		List<CoreStatDTO> coreStats = cpuService.getCoreCpuDistribution();

		int totalCpuCount = cpuList.size();

		model.addAttribute("cpus", cpuList);
		model.addAttribute("totalCpuCount", totalCpuCount);
		model.addAttribute("coreStats", coreStats);

		return "cpu/cpu-list";
	}

	/**
	 * 단일 프로세서(P2)에 대한 상세 정보
	 * 
	 * @param cpuId 단일 프로세서의 고유ID
	 * @param model 단일 프로세서 모델
	 * @return cpu-detail-current
	 */
	@GetMapping("/cpus/{cpuId}")
	public String cpuDetail(@PathVariable Integer cpuId, Model model) {
//	public String cpuDetail(@PathVariable Integer cpuId,
//            HttpSession session,
//            Model model) {
		
//		Integer appUserId = (Integer) session.getAttribute("appUserId");
//		CpuDTO cpu = cpuService.getCpuById(cpuId, appUserId);
		
		CpuDTO cpu = cpuService.getCpuById(cpuId);
		String cpuName = cpu.getNameCpu();

		List<DeviceDTO> devices = deviceService.getDevicesByCpuName(cpuName);
		Map<String, Integer> brandCounts = deviceService.getDeviceCountByBrand(cpuName);

		model.addAttribute("cpu", cpu);
		model.addAttribute("devices", devices);
		model.addAttribute("brandCounts", brandCounts);

		return "cpu/cpu-detail-current";
	}

	// -----------------------------------------------

	/**
	 * js를 활용해 브라우저(클라이언트) 렌더링<br>
	 * fetch('/api/cpus') 호출을 통해 REST API 응답(JSON)을 받아옴<br>
	 * ECMAScript의 Fetch API 기반 비동기 요청 방식의 CSR(Promise)<br>
	 * 
	 * @return
	 */
	@GetMapping("/cpus-fetch")
	public String cpuRestPageFetch() {
		return "cpu/cpu-list-fetch";
	}

	/**
	 * js를 활용해 브라우저(클라이언트) 렌더링<br>
	 * $.ajax('/api/cpus') 호출을 통해 REST API 응답(JSON)을 받아옴<br>
	 * jQuery 라이브러리 기반 AJAX 요청 방식의 CSR(XMLHttpRequest)<br>
	 * 
	 * @return
	 */
	@GetMapping("/cpus-jquery")
	public String cpuRestPageJquery() {
		return "cpu/cpu-list-jquery";
	}

	/**
	 * @param name
	 * @param model
	 * @return
	 */
	@GetMapping("/cpu-core-graph/{name}")
	public String cpuCoreGraph(@PathVariable String name, Model model) {
		CpuDTO cpu = cpuService.getCpuByName(name);
		if (cpu == null) {
			return "error/404";
		}

		model.addAttribute("coreCpu", cpu.getCoreCpu());
		model.addAttribute("nameCpu", cpu.getNameCpu());
		return "cpu/core-graph";
	}

	/**
	 * @param catgo
	 * @param search
	 * @param model
	 * @return
	 */
	@GetMapping("/search/cpu")
	public String searchCpu(@RequestParam("catgo") String catgo, @RequestParam("search") String search, Model model) {
		List<CpuDTO> cpuResults;

		if ("name_cpu".equals(catgo)) {
			cpuResults = cpuService.getCpuListByName(search);
		} else if ("manf_cpu".equals(catgo)) {
			cpuResults = cpuService.getCpuListByManufacturer(search);
		} else if ("core_cpu".equals(catgo)) {
			cpuResults = cpuService.getCpuListByCore(Integer.parseInt(search));
		} else if ("thread_cpu".equals(catgo)) {
			cpuResults = cpuService.getCpuListByThread(Integer.parseInt(search));
		} else if ("release_cpu".equals(catgo)) {
			cpuResults = cpuService.getCpuListByRelease(Integer.parseInt(search));
		} else {
			cpuResults = List.of();
		}

		model.addAttribute("search", search);
		model.addAttribute("cpuResults", cpuResults);
		return "search/cpuResult";
	}

}
