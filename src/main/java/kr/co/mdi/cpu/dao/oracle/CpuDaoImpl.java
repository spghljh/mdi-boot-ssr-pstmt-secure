package kr.co.mdi.cpu.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import kr.co.mdi.common.jdbc.AbstractJdbcDao;
import kr.co.mdi.cpu.dao.CpuDao;
import kr.co.mdi.cpu.dto.CoreStatDTO;
import kr.co.mdi.cpu.dto.CpuDTO;

@Profile("dev-user-oracle")
@Repository
public class CpuDaoImpl extends AbstractJdbcDao implements CpuDao {

//	private final DataSource dataSource;
//
//	@Autowired
//	public CpuDaoOracleImpl(DataSource dataSource) {
//		this.dataSource = dataSource;
//	}
//
//	public Connection getConnection() throws SQLException {
//		return dataSource.getConnection(); // 커넥션 풀에서 가져옴
//	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public int selectTotalCpuCount() {
		String sql = "SELECT COUNT(*) FROM cpu";
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException("CPU row 수 조회 중 오류 발생", e);
		}
		return 0;
	}

	@Override
	public List<CpuDTO> selectAllCpus() {
		List<CpuDTO> cpuList = new ArrayList<>();
		String sql = """
				    SELECT
				        m.id_cpu,
				        m.name_cpu,
				        m.release_cpu,
				        m.core_cpu,
				        m.thread_cpu,
				        m.maxghz_cpu,
				        m.minghz_cpu,
				        m.choice_cpu,
				        m.cpu_type_code,
				        t.type_cpu,
				        m.cpu_manf_code,
				        b.manf_cpu
				    FROM cpu m
				    LEFT JOIN cpu_type t ON m.cpu_type_code = t.cpu_type_code
				    LEFT JOIN cpu_manf_brand b ON m.cpu_manf_code = b.cpu_manf_code
				""";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
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
				cpuList.add(cpu);
			}

		} catch (SQLException se) {
			se.printStackTrace(); // 필요 시 로깅 처리
			throw new RuntimeException("DB 조회 중 오류 발생", se);
		}

		return cpuList;
	}

	@Override
	public CpuDTO selectCpuById(Integer cpuId) {
		String sql = """
				    SELECT
				        m.id_cpu,
				        m.name_cpu,
				        m.release_cpu,
				        m.core_cpu,
				        m.thread_cpu,
				        m.maxghz_cpu,
				        m.minghz_cpu,
				        m.choice_cpu,
				        m.cpu_type_code,
				        t.type_cpu,
				        m.cpu_manf_code,
				        b.manf_cpu
				    FROM cpu m
				    LEFT JOIN cpu_type t ON m.cpu_type_code = t.cpu_type_code
				    LEFT JOIN cpu_manf_brand b ON m.cpu_manf_code = b.cpu_manf_code
				    WHERE m.id_cpu = ?
				""";

		CpuDTO cpu = null;

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, cpuId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					cpu = new CpuDTO();
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
				}
			}

		} catch (SQLException se) {
			se.printStackTrace();
			throw new RuntimeException("DB 조회 중 오류 발생2", se);
		}

		return cpu;
	}
	
	//---------------------------------
	
		@Override
		public CpuDTO selectCpuByName(String nameCpu) {
		    String sql = "SELECT * FROM cpu WHERE name_cpu = ?";
		    try (Connection conn = dataSource.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        pstmt.setString(1, nameCpu);
		        try (ResultSet rs = pstmt.executeQuery()) {
		            if (rs.next()) {
		                CpuDTO cpu = new CpuDTO();
		                cpu.setIdCpu(rs.getInt("id_cpu"));
		                cpu.setNameCpu(rs.getString("name_cpu"));
		                cpu.setManfCpu(rs.getString("manf_cpu"));
		                cpu.setCoreCpu(rs.getInt("core_cpu"));
		                cpu.setThreadCpu(rs.getInt("thread_cpu"));
		                cpu.setMaxghzCpu(rs.getFloat("maxghz_cpu"));
		                cpu.setMinghzCpu(rs.getFloat("minghz_cpu"));
		                cpu.setReleaseCpu(rs.getInt("release_cpu"));
		                cpu.setChoiceCpu(rs.getInt("choice_cpu"));
		                return cpu;
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace(); // 로깅 처리 권장
		    }
		    return null;
		}

		//
		
		@Override
		public List<CpuDTO> selectCpuListByName(String nameCpu) {
		    List<CpuDTO> cpuList = new ArrayList<>();
		    String sql = "SELECT * FROM cpu WHERE name_cpu LIKE ? ORDER BY id_cpu ASC";

		    try (Connection conn = dataSource.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        pstmt.setString(1, "%" + nameCpu + "%");
		        ResultSet rs = pstmt.executeQuery();

		        while (rs.next()) {
		            CpuDTO dto = new CpuDTO();
		            dto.setIdCpu(rs.getInt("id_cpu"));
		            dto.setNameCpu(rs.getString("name_cpu"));
		            dto.setManfCpu(rs.getString("manf_cpu"));
		            dto.setCoreCpu(rs.getInt("core_cpu"));
		            dto.setThreadCpu(rs.getInt("thread_cpu"));
		            dto.setMaxghzCpu(rs.getFloat("maxghz_cpu"));
		            dto.setMinghzCpu(rs.getFloat("minghz_cpu"));
		            cpuList.add(dto);
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return cpuList;
		}
		
		@Override
		public List<CpuDTO> selectHotCpuList() {
		    List<CpuDTO> hotCpuList = new ArrayList<>();

		    String sql = """
		        SELECT
		            m.id_cpu,
		            m.name_cpu,
		            m.choice_cpu,
		            m.cpu_manf_code,
		            b.manf_cpu
		        FROM cpu m
		        LEFT JOIN cpu_manf_brand b ON m.cpu_manf_code = b.cpu_manf_code
		        ORDER BY m.choice_cpu DESC
		        LIMIT 10
		    """;

		    try (Connection conn = getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql);
		         ResultSet rs = pstmt.executeQuery()) {

		        while (rs.next()) {
		            CpuDTO dto = new CpuDTO();
		            dto.setIdCpu(rs.getInt("id_cpu"));
		            dto.setNameCpu(rs.getString("name_cpu"));
		            dto.setChoiceCpu(rs.getInt("choice_cpu"));
		            dto.setCpuManfCode(rs.getString("cpu_manf_code"));
		            dto.setManfCpu(rs.getString("manf_cpu"));
		            hotCpuList.add(dto);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		        throw new RuntimeException("인기 CPU 목록 조회 중 오류 발생", e);
		    }

		    return hotCpuList;
		}
		
		//
		
		@Override
		public List<CpuDTO> selectCpuListByManufacturer(String manfCpu) {
		    List<CpuDTO> list = new ArrayList<>();
		    String sql = "SELECT * FROM cpu WHERE manf_cpu = ?";

		    try (Connection conn = dataSource.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        pstmt.setString(1, manfCpu);

		        try (ResultSet rs = pstmt.executeQuery()) {
		            while (rs.next()) {
		                CpuDTO cpu = new CpuDTO();
		                cpu.setIdCpu(rs.getInt("id_cpu"));
		                cpu.setNameCpu(rs.getString("name_cpu"));
		                cpu.setManfCpu(rs.getString("manf_cpu"));
		                cpu.setCoreCpu(rs.getInt("core_cpu"));
		                cpu.setMaxghzCpu(rs.getFloat("maxghz_cpu"));
		                // 필요한 필드 추가
		                list.add(cpu);
		            }
		        }

		    } catch (SQLException e) {
		        e.printStackTrace(); // 로깅 또는 예외 처리
		    }

		    return list;
		}
		
		//
		
		@Override
		public List<CpuDTO> selectCpuListByCore(int coreCpu) {
		    List<CpuDTO> list = new ArrayList<>();
		    String sql = """
		        SELECT c.*, b.manf_cpu
		        FROM cpu c
		        JOIN cpu_manf_brand b ON c.cpu_manf_code = b.cpu_manf_code
		        WHERE c.core_cpu = ?
		    """;

		    try (Connection conn = dataSource.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        pstmt.setInt(1, coreCpu);

		        try (ResultSet rs = pstmt.executeQuery()) {
		            while (rs.next()) {
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
		                cpu.setCpuManfCode(rs.getString("cpu_manf_code"));
		                cpu.setManfCpu(rs.getString("manf_cpu"));
		                list.add(cpu);
		            }
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return list;
		}

		@Override
		public List<CpuDTO> selectCpuListByThread(int threadCpu) {
		    List<CpuDTO> list = new ArrayList<>();
		    String sql = """
		        SELECT c.*, b.manf_cpu
		        FROM cpu c
		        JOIN cpu_manf_brand b ON c.cpu_manf_code = b.cpu_manf_code
		        WHERE c.thread_cpu = ?
		    """;

		    try (Connection conn = dataSource.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        pstmt.setInt(1, threadCpu);

		        try (ResultSet rs = pstmt.executeQuery()) {
		            while (rs.next()) {
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
		                cpu.setCpuManfCode(rs.getString("cpu_manf_code"));
		                cpu.setManfCpu(rs.getString("manf_cpu"));
		                list.add(cpu);
		            }
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return list;
		}

		
		@Override
		public List<CpuDTO> selectCpuListByRelease(int releaseCpu) {
		    List<CpuDTO> list = new ArrayList<>();
		    String sql = """
		        SELECT c.*, b.manf_cpu
		        FROM cpu c
		        JOIN cpu_manf_brand b ON c.cpu_manf_code = b.cpu_manf_code
		        WHERE c.release_cpu = ?
		    """;

		    try (Connection conn = dataSource.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        pstmt.setInt(1, releaseCpu);

		        try (ResultSet rs = pstmt.executeQuery()) {
		            while (rs.next()) {
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
		                cpu.setCpuManfCode(rs.getString("cpu_manf_code"));
		                cpu.setManfCpu(rs.getString("manf_cpu"));
		                list.add(cpu);
		            }
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return list;
		}
		
		//

		@Override
		public List<CoreStatDTO> getCoreCpuDistribution() {
			List<CoreStatDTO> list = new ArrayList<>();
			String sql = """
					    SELECT core_cpu, COUNT(*) AS count
					    FROM cpu
					    GROUP BY core_cpu
					    ORDER BY core_cpu
					""";

			try (Connection conn = dataSource.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {
					CoreStatDTO stat = new CoreStatDTO();
					stat.setCoreCpu(rs.getInt("core_cpu"));
					stat.setCount(rs.getInt("count"));
					list.add(stat);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return list;
		}

}
