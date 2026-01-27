package kr.co.mdi.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.mdi.cpu.dto.CpuDTO;
import kr.co.mdi.cpu.service.CpuService;
import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.device.service.DeviceService;

@Controller
public class MainController {

	@Autowired
	private CpuService cpuService;

	@Autowired
	private DeviceService deviceService;

	// 메인 페이지
	@GetMapping("/")
	public String index(Model model) {

		int totalCpuCount = cpuService.getTotalCpuCount();
		int totalDeviceCount = deviceService.getTotalDeviceCount();
		model.addAttribute("totalCpuCount", totalCpuCount);
		model.addAttribute("totalDeviceCount", totalDeviceCount);

		List<CpuDTO> hotCpuList = cpuService.getHotCpuList();
		List<DeviceDTO> hotDeviceList = deviceService.getHotDeviceList();
		model.addAttribute("hotCpuList", hotCpuList);
		model.addAttribute("hotDeviceList", hotDeviceList);
		return "index";
	}

	// 로그인 페이지
	@GetMapping("/login")
	public String loginPage() {
		return "member/member-login";
	}

//    // BASE 페이지
//    @GetMapping("/base")
//    public String basePage() {
//        return "base/base";
//    }

}
