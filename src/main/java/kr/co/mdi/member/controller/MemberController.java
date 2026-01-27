package kr.co.mdi.member.controller;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
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

	@PostMapping("/register")
	public String processRegister(@ModelAttribute MemberDTO user) {
		userService.registerUser(user);
		return "redirect:/login";
	}

	@GetMapping("/check-id")
	@ResponseBody
	public boolean checkId(@RequestParam String id) {
		return userService.isDuplicateId(id);
	}

	@PostMapping("/loginProc")
	public String processLogin(@RequestParam String id, @RequestParam String pass, HttpSession session, Model model) {

		MemberDTO user = userService.findUserById(id); // 아이디로 사용자 조회

//		if (user == null) {
//			model.addAttribute("error", "존재하지 않는 아이디입니다.");
//			return "login"; // 로그인 페이지로 다시 이동
//		}
//
//		if (!BCrypt.checkpw(pass, user.getPass())) {
//			model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
//			return "login";
//		}

		// 아이디 존재 여부를 노출하지 않도록 처리하는 게 실무 보안 기준
		if (user == null || !BCrypt.checkpw(pass, user.getPass())) {
			model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
			return "redirect:/login"; // 로그인 페이지로 이동
		}

		// 로그인 성공 → 세션에 사용자 정보 저장

		// 세션 저장 시 최소 정보만 저장
//		session.setAttribute("loginUser", user);
		session.setAttribute("loginUser", user.getId());

		return "redirect:/"; // 메인 페이지로 이동
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 세션 초기화
		return "redirect:/login"; // 로그인 페이지로 이동
	}
	
//    @GetMapping("/member/detail")
//    public String loginDetail(HttpSession session, Model model) {
//        Object loginUser = session.getAttribute("loginUser");
//        if (loginUser == null) {
//            return "redirect:/login";
//        }
//        model.addAttribute("loginUser", loginUser);
//        return "member/member-detail";
//    }
	
//	@GetMapping("/member/detail")
//	public String loginDetail(HttpSession session, Model model) {
//	    String loginUserId = (String) session.getAttribute("loginUser");
//	    if (loginUserId == null) {
//	        return "redirect:/login";
//	    }
//
//	    model.addAttribute("loginUser", loginUserId);
//
//	    // 관심 CPU 목록 조회
//	    List<String> favoriteCpus = userService.getFavoriteCpuNames(loginUserId);
//	    model.addAttribute("favoriteCpus", favoriteCpus);
//
//	    return "member/member-detail";
//	}
	
	@GetMapping("/member/detail")
	public String loginDetail(HttpSession session, Model model) {
	    String loginUserId = (String) session.getAttribute("loginUser");
	    if (loginUserId == null) {
	        return "redirect:/login";
	    }

	    model.addAttribute("loginUser", loginUserId);

	    List<CpuDTO> favoriteCpus = userService.getFavoriteCpuDetails(loginUserId);
	    List<DeviceDTO> favoriteDevices = userService.getFavoriteDeviceDetails(loginUserId);
	    
	    
	    model.addAttribute("favoriteCpus", favoriteCpus);
	    model.addAttribute("favoriteDevices", favoriteDevices);

	    return "member/member-detail";
	}


    
    @GetMapping("/member/modify")
    public String loginModify(HttpSession session, Model model) {
        // 세션에서 로그인한 사용자 ID 가져오기
        String loginUserId = (String) session.getAttribute("loginUser");
        if (loginUserId == null) {
            return "redirect:/login";
        }

        // 사용자 ID로 MemberDTO 조회
        MemberDTO member = userService.findUserById(loginUserId); // 예: DB에서 SELECT * WHERE id = ?

        // 모델에 MemberDTO 담기
        model.addAttribute("member", member);
        return "member/member-modify"; // templates/member/member-modify.html
    }

    //
    
//    @PostMapping("/member/add-cpu")
//    public String addCpuToFavorites(@RequestParam int cpuId, HttpSession session) {
//        String loginUserId = (String) session.getAttribute("loginUser");
//        if (loginUserId == null) {
//            return "redirect:/login";
//        }
//
//        userService.addCpuPreference(loginUserId, cpuId);
//        return "redirect:/member/detail";
//    }
    
    @PostMapping("/member/add-cpu")
    public String addCpuToFavorites(@RequestParam int cpuId, HttpSession session, RedirectAttributes redirectAttributes) {
        String loginUserId = (String) session.getAttribute("loginUser");
        if (loginUserId == null) {
            return "redirect:/login";
        }

        boolean added = userService.addCpuPreference(loginUserId, cpuId);
        if (!added) {
            redirectAttributes.addFlashAttribute("message", "이미 관심 CPU로 추가된 항목입니다.");
        }

//        return "redirect:/member/detail";
        return "redirect:/cpus/" + cpuId;
    }

    
    @PostMapping("/member/delete-cpu/{cpuId}")
    public String deleteCpuPreference(@PathVariable int cpuId, HttpSession session) {
        String loginUserId = (String) session.getAttribute("loginUser");
        if (loginUserId == null) {
            return "redirect:/login";
        }

        userService.removeCpuPreference(loginUserId, cpuId);
        return "redirect:/member/detail";
    }

    //
    
    @PostMapping("/member/add-device")
    public String addDeviceToFavorites(@RequestParam int deviceId, HttpSession session, RedirectAttributes redirectAttributes) {
        String loginUserId = (String) session.getAttribute("loginUser");
        if (loginUserId == null) {
            return "redirect:/login";
        }

        boolean added = userService.addDevicePreference(loginUserId, deviceId);
        if (!added) {
            redirectAttributes.addFlashAttribute("message", "이미 관심 디바이스로 추가된 항목입니다.");
        }

        return "redirect:/devices/" + deviceId;
    }

    @PostMapping("/member/delete-device/{deviceId}")
    public String deleteDevicePreference(@PathVariable int deviceId, HttpSession session) {
        String loginUserId = (String) session.getAttribute("loginUser");
        if (loginUserId == null) {
            return "redirect:/login";
        }

        userService.removeDevicePreference(loginUserId, deviceId);
        return "redirect:/member/detail";
    }


    
}
