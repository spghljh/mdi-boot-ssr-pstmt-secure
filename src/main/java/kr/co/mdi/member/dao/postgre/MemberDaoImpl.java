package kr.co.mdi.member.dao.postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import kr.co.mdi.common.jdbc.AbstractJdbcDao;
import kr.co.mdi.cpu.dto.CpuDTO;
import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.member.dao.MemberDao;
import kr.co.mdi.member.dao.SequenceBasedMemberDao;
import kr.co.mdi.member.dto.MemberDTO;

@Profile("dev-user-postgre")
@Repository
public class MemberDaoImpl extends AbstractJdbcDao implements MemberDao, SequenceBasedMemberDao {

//    @Value("${current.schema}")
//    private String currentSchema;

//    @Override
//    public Connection getConnection() throws SQLException {
//        Connection conn = dataSource.getConnection();
//        try (PreparedStatement stmt = conn.prepareStatement("SET search_path TO " + currentSchema)) {
//            stmt.execute();
//        }
//        return conn;
//    }
	
    private final DataSource dataSource;

    public MemberDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection(); // SET search_path 생략
    }

	@Override
	public int getNextMemberId() {
		String sql = "SELECT nextval('seq_id_member')";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			} else {
				throw new RuntimeException("시퀀스 값을 가져올 수 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("시퀀스 조회 중 오류 발생", e);
		}
	}

	@Override
	public void insertUser(MemberDTO member) {
		String sql = """
				    INSERT INTO member (
				        id_member, id, pass, name, email,
				        role, status, email_verified, fail_count, regist_day
				    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, member.getIdMember());
			pstmt.setString(2, member.getId());
			pstmt.setString(3, member.getPass());
			pstmt.setString(4, member.getName());
			pstmt.setString(5, member.getEmail());
			pstmt.setString(6, member.getRole());
			pstmt.setString(7, member.getStatus());
			pstmt.setString(8, member.getEmailVerified());
			pstmt.setInt(9, member.getFailCount());
			pstmt.setTimestamp(10, java.sql.Timestamp.valueOf(member.getRegistDay()));

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("회원가입 중 오류 발생", e);
		}
	}

	@Override
	public boolean existsById(String id) {
		String sql = "SELECT COUNT(*) FROM member WHERE id = ?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("아이디 중복 체크 중 오류 발생", e);
		}
		return false;
	}

	@Override
	public MemberDTO findById(String id) {
		String sql = "SELECT * FROM member WHERE id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				MemberDTO member = new MemberDTO();
				member.setIdMember(rs.getInt("id_member"));
				member.setId(rs.getString("id"));
				member.setPass(rs.getString("pass"));
				member.setName(rs.getString("name"));
				member.setEmail(rs.getString("email"));
				member.setRole(rs.getString("role"));
				member.setStatus(rs.getString("status"));
				member.setEmailVerified(rs.getString("email_verified"));
				member.setFailCount(rs.getInt("fail_count"));

				member.setRegistDay(toLocalDateTime(rs.getTimestamp("regist_day")));
				member.setLastLogin(toLocalDateTime(rs.getTimestamp("last_login")));
				member.setUpdatedAt(toLocalDateTime(rs.getTimestamp("updated_at")));
				member.setDeletedAt(toLocalDateTime(rs.getTimestamp("deleted_at")));

				return member;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("회원 조회 중 오류 발생", e);
		}

		return null;
	}

	// 헬퍼 메서드
	private LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp) {
		return timestamp != null ? timestamp.toLocalDateTime() : null;
	}
	
	// -------------
	
	@Override
	public void insertCpuPreference(String memberId, int cpuId) {
	    String sql = "INSERT INTO member_cpu_preference (member_id, cpu_id) VALUES (?, ?)";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, memberId);
	        pstmt.setInt(2, cpuId);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("관심 CPU 추가 중 오류 발생", e);
	    }
	}

	@Override
	public boolean existsCpuPreference(String memberId, int cpuId) {
	    String sql = "SELECT COUNT(*) FROM member_cpu_preference WHERE member_id = ? AND cpu_id = ?";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, memberId);
	        pstmt.setInt(2, cpuId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("관심 CPU 중복 확인 중 오류 발생", e);
	    }
	    return false;
	}

	@Override
	public void incrementCpuChoiceCount(int cpuId) {
	    String sql = "UPDATE mcl SET choice_cpu = choice_cpu + 1 WHERE id_cpu = ?";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, cpuId);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("CPU 선택 수 증가 중 오류 발생", e);
	    }
	}
	
	//
	
	@Override
	public int getIdMemberById(String id) {
	    String sql = "SELECT id_member FROM member WHERE id = ?";
	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, id);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("id_member");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("회원 ID 조회 중 오류 발생", e);
	    }
	    throw new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + id);
	}
	
	@Override
	public List<Integer> findCpuPreferences(int idMember) {
	    String sql = "SELECT cpu_id FROM cpu_prefer WHERE id_member = ?";
	    List<Integer> cpuIds = new ArrayList<>();

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idMember);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                cpuIds.add(rs.getInt("cpu_id"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("관심 CPU 목록 조회 중 오류 발생", e);
	    }

	    return cpuIds;
	}
	
	@Override
	public String findCpuNameById(int cpuId) {
	    String sql = "SELECT name_cpu FROM cpu WHERE id_cpu = ?";
	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, cpuId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("name_cpu");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("CPU 이름 조회 중 오류 발생", e);
	    }
	    return null;
	}
	
//
	
	@Override
	public CpuDTO findCpuDetailById(int cpuId) {
	    String sql = """
	        SELECT c.*, t.type_cpu, m.manf_cpu
	        FROM cpu c
	        LEFT JOIN cpu_type t ON c.cpu_type_code = t.cpu_type_code
	        LEFT JOIN cpu_manf_brand m ON c.cpu_manf_code = m.cpu_manf_code
	        WHERE c.id_cpu = ?
	    """;

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, cpuId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                CpuDTO cpu = new CpuDTO();
	                cpu.setIdCpu(rs.getInt("id_cpu"));
	                cpu.setNameCpu(rs.getString("name_cpu"));
	                cpu.setReleaseCpu(rs.getInt("release_cpu"));
	                cpu.setCoreCpu(rs.getInt("core_cpu"));
	                cpu.setThreadCpu(rs.getInt("thread_cpu"));
	                cpu.setMaxghzCpu(rs.getFloat("maxghz_cpu"));
	                cpu.setMinghzCpu(rs.getFloat("minghz_cpu"));
	                cpu.setChoiceCpu(rs.getInt("choice_cpu"));
	                cpu.setCpuTypeCode(rs.getString("cpu_type_code"));
	                cpu.setTypeCpu(rs.getString("type_cpu"));
	                cpu.setCpuManfCode(rs.getString("cpu_manf_code"));
	                cpu.setManfCpu(rs.getString("manf_cpu"));
	                return cpu;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("CPU 상세 조회 중 오류 발생", e);
	    }
	    return null;
	}
	
	// -----------
	
	@Override
	public void deleteCpuPreference(String memberId, int cpuId) {
	    int idMember = getIdMemberById(memberId);
	    String sql = "DELETE FROM cpu_prefer WHERE id_member = ? AND cpu_id = ?";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idMember);
	        pstmt.setInt(2, cpuId);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("관심 CPU 삭제 중 오류 발생", e);
	    }
	}

	@Override
	public void decrementCpuChoiceCount(int cpuId) {
	    String sql = "UPDATE cpu SET choice_cpu = choice_cpu - 1 WHERE id_cpu = ? AND choice_cpu > 0";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, cpuId);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("CPU 선택 수 감소 중 오류 발생", e);
	    }
	}
	
	//
	
	@Override
	public void insertDevicePreference(String memberId, int deviceId) {
	    int idMember = getIdMemberById(memberId);
	    String sql = "INSERT INTO device_prefer (id_member, device_id) VALUES (?, ?)";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idMember);
	        pstmt.setInt(2, deviceId);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("관심 디바이스 추가 중 오류 발생", e);
	    }
	}

	
	@Override
	public boolean existsDevicePreference(String memberId, int deviceId) {
	    int idMember = getIdMemberById(memberId);
	    String sql = "SELECT COUNT(*) FROM device_prefer WHERE id_member = ? AND device_id = ?";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idMember);
	        pstmt.setInt(2, deviceId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("관심 디바이스 중복 확인 중 오류 발생", e);
	    }
	    return false;
	}

	@Override
	public void incrementDeviceChoiceCount(int deviceId) {
	    String sql = "UPDATE device SET choice_device = choice_device + 1 WHERE id_device = ?";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, deviceId);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("디바이스 선택 수 증가 중 오류 발생", e);
	    }
	}

	
	@Override
	public void decrementDeviceChoiceCount(int deviceId) {
	    String sql = "UPDATE device SET choice_device = choice_device - 1 WHERE id_device = ? AND choice_device > 0";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, deviceId);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("디바이스 선택 수 감소 중 오류 발생", e);
	    }
	}

	
	@Override
	public List<Integer> findDevicePreferences(int idMember) {
	    String sql = "SELECT device_id FROM device_prefer WHERE id_member = ?";
	    List<Integer> deviceIds = new ArrayList<>();

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idMember);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                deviceIds.add(rs.getInt("device_id"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("관심 디바이스 목록 조회 중 오류 발생", e);
	    }

	    return deviceIds;
	}

	
	@Override
	public DeviceDTO findDeviceDetailById(int deviceId) {
	    String sql = """
	        SELECT d.*, t.type_device, m.manf_device
	        FROM device d
	        LEFT JOIN device_type t ON d.device_type_code = t.device_type_code
	        LEFT JOIN device_manf_brand m ON d.device_manf_code = m.device_manf_code
	        WHERE d.id_device = ?
	    """;

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, deviceId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                DeviceDTO device = new DeviceDTO();
	                device.setIdDevice(rs.getInt("id_device"));
	                device.setNameDevice(rs.getString("name_device"));
	                device.setReleaseDevice(rs.getInt("release_device"));
	                device.setChoiceDevice(rs.getInt("choice_device"));
	                device.setDeviceTypeCode(rs.getString("device_type_code"));
	                device.setTypeDevice(rs.getString("type_device"));
	                device.setDeviceManfCode(rs.getString("device_manf_code"));
	                device.setManfDevice(rs.getString("manf_device"));
	                return device;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("디바이스 상세 조회 중 오류 발생", e);
	    }
	    return null;
	}

	@Override
	public String findDeviceNameById(int deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDevicePreference(String memberId, int deviceId) {
	    int idMember = getIdMemberById(memberId);
	    String sql = "DELETE FROM device_prefer WHERE id_member = ? AND device_id = ?";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, idMember);
	        pstmt.setInt(2, deviceId);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("관심 디바이스 삭제 중 오류 발생", e);
	    }
	}
}
