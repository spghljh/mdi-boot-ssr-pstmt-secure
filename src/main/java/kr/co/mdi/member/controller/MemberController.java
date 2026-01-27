package kr.co.mdi.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.mdi.cpu.dto.CpuDTO;
import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.member.dto.MemberDTO;
import kr.co.mdi.member.service.MemberService;

@Controller
public class MemberController {

    @Autowired
    private MemberService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new MemberDTO());
        return "member/member-register";
    }

//    @PostMapping("/register")
//    public String register(@ModelAttribute MemberDTO user) {
//    	userService.registerUser(user);
//        return "redirect:/login?registered";
//    }
    
//    @PostMapping("/register")
//    public String processRegister(@ModelAttribute MemberDTO user) {
//        userService.registerUser(user);
//        return "redirect:/login";
//    }
    
    @PostMapping("/register")
    public String processRegister(@ModelAttribute MemberDTO user) {
        userService.registerUser(user); // DB 저장
        return "member/member-register-success"; //회원가입 완료 페이지로 이동
    }

    @GetMapping("/check-id")
    @ResponseBody
    public boolean checkId(@RequestParam String id) {
        return userService.isDuplicateId(id);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "member/member-login"; // templates/member/member-login.html
    }

    // 로그아웃은 Spring Security가 처리하므로 컨트롤러 메서드 불필요
    // SecurityConfig에서 logoutUrl("/logout") 설정으로 자동 처리됨

    @GetMapping("/member/detail")
    public String loginDetail(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginUserId = auth.getName();

        MemberDTO member = userService.findUserById(loginUserId);
        model.addAttribute("loginUser", loginUserId);
        model.addAttribute("member", member);

        List<CpuDTO> favoriteCpus = userService.getFavoriteCpuDetails(loginUserId);
        List<DeviceDTO> favoriteDevices = userService.getFavoriteDeviceDetails(loginUserId);

        model.addAttribute("favoriteCpus", favoriteCpus);
        model.addAttribute("favoriteDevices", favoriteDevices);

        return "member/member-detail";
    }

    @GetMapping("/member/modify")
    public String showModifyForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginUserId = auth.getName();

        MemberDTO member = userService.findUserById(loginUserId);
        model.addAttribute("member", member);

        return "member/member-modify"; // 수정 폼 페이지
    }

//    @PostMapping("/member/modify")
//    public String processModify(@ModelAttribute MemberDTO member) {
//        userService.updateUser(member); // DB 업데이트 로직
//        return "redirect:/member/detail"; // 수정 후 상세 페이지로 이동
//    }
    
    @PostMapping("/member/modify")
    public String processModify(@ModelAttribute MemberDTO member) {
    	
        // 로그인한 사용자 ID를 세션에서 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginUserId = auth.getName();
        member.setId(loginUserId); // 안전하게 ID를 주입

        userService.updateUser(member); // DB 업데이트 실행
        return "redirect:/member/detail"; // 수정 후 상세 페이지로 이동
    }

    

    @PostMapping("/member/add-cpu")
    public String addCpuToFavorites(@RequestParam int cpuId, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginUserId = auth.getName();

        boolean added = userService.addCpuPreference(loginUserId, cpuId);
        if (!added) {
            redirectAttributes.addFlashAttribute("message", "이미 관심 CPU로 추가된 항목입니다.");
        }
        return "redirect:/cpus/" + cpuId;
    }

    @PostMapping("/member/delete-cpu/{cpuId}")
    public String deleteCpuPreference(@PathVariable int cpuId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginUserId = auth.getName();

        userService.removeCpuPreference(loginUserId, cpuId);
        return "redirect:/member/detail";
    }

    @PostMapping("/member/add-device")
    public String addDeviceToFavorites(@RequestParam int deviceId, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginUserId = auth.getName();

        boolean added = userService.addDevicePreference(loginUserId, deviceId);
        if (!added) {
            redirectAttributes.addFlashAttribute("message", "이미 관심 디바이스로 추가된 항목입니다.");
        }
        return "redirect:/devices/" + deviceId;
    }

    @PostMapping("/member/delete-device/{deviceId}")
    public String deleteDevicePreference(@PathVariable int deviceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginUserId = auth.getName();

        userService.removeDevicePreference(loginUserId, deviceId);
        return "redirect:/member/detail";
    }
}
