package kr.co.mdi.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.mdi.cpu.service.CpuService;
import kr.co.mdi.device.service.DeviceService;

@Controller
public class BaseController {

    @Autowired
    private CpuService cpuService;

    @Autowired
    private DeviceService deviceService;

    // BASE 페이지 (CPU + DEVICE 통합)
    @GetMapping("/base")
    public String basePage(Model model) {
        model.addAttribute("cpus", cpuService.getCpuList());
        model.addAttribute("devices", deviceService.getDeviceList());
        return "base/base-list";
    }
}
