package kr.co.mdi.cpu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mdi.cpu.dto.CpuDTO;
import kr.co.mdi.cpu.service.CpuService;

/**
 * REST API 컨트롤러<br>
 * (REST 원칙을 따르는 API)를 제공하는 컨트롤러 클래스<br>
 * REST API 엔드포인트(URIdentifier) : /api/cpus
 */
@RestController
@RequestMapping("/api/cpus")
public class CpuRestController {

	@Autowired
	private CpuService cpuService;
	
	/**
	 * REST API 메서드<br>
	 * 클라이언트 호출 함수(fetch, ajax)에 직렬화된 JSON 응답 전달<br>
	 * POJO getter 기반 Jackson 라이브러리를 통한 JSON 키-값 쌍<br>
	 * 뷰 명칭이 아닌 HTTP 응답 본문으로 전달<br>
	 * @return DTO 객체들의 리스트(직렬화된 JSON 배열)
	 */
	@GetMapping
	public List<CpuDTO> getCpuList() {
		return cpuService.getCpuList();
	}

}
