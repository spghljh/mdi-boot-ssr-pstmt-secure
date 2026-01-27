package kr.co.mdi.member.dao;

import java.util.List;

import kr.co.mdi.cpu.dto.CpuDTO;
import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.member.dto.MemberDTO;

public interface MemberDao {

	void insertUser(MemberDTO member);

	boolean existsById(String id); // 아이디 중복 체크용

	MemberDTO findById(String id);

	// 관심 CPU 관련 메서드 (이번 단계에서 필요한 것만)
	void insertCpuPreference(String memberId, int cpuId);

	boolean existsCpuPreference(String memberId, int cpuId);

	void incrementCpuChoiceCount(int cpuId);

	// 시퀀스 값 가져오기
	// Mysql은 미적용
	// 인터페이스 분리(SequenceBasedMemberDao)
	// SOLID 원칙 중 ISP
//	int getNextMemberId();

	//

//	void insert(String memberId, Integer itemId);
//
//	void delete(String memberId, Integer itemId);
//
//	List<Integer> findByMemberId(String memberId);

	//

	int getIdMemberById(String id);

	List<Integer> findCpuPreferences(int idMember);

	String findCpuNameById(int cpuId);

	CpuDTO findCpuDetailById(int cpuId);

	void deleteCpuPreference(String memberId, int cpuId);

	void decrementCpuChoiceCount(int cpuId);
	
	// 관심 디바이스 추가
	void insertDevicePreference(String memberId, int deviceId);

	// 관심 디바이스 존재 여부 확인
	boolean existsDevicePreference(String memberId, int deviceId);

	// 디바이스 선택 수 증가
	void incrementDeviceChoiceCount(int deviceId);

	// 디바이스 선택 수 감소
	void decrementDeviceChoiceCount(int deviceId);

	// 회원 ID로 관심 디바이스 ID 목록 조회
	List<Integer> findDevicePreferences(int idMember);

	// 디바이스 ID로 디바이스 이름 조회 (선택 사항)
	String findDeviceNameById(int deviceId);

	// 디바이스 ID로 상세 정보 조회
	DeviceDTO findDeviceDetailById(int deviceId);

	// 관심 디바이스 삭제
	void deleteDevicePreference(String memberId, int deviceId);


}
