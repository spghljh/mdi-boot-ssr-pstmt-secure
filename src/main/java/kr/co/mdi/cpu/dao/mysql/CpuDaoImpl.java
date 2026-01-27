package kr.co.mdi.cpu.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import kr.co.mdi.common.jdbc.AbstractJdbcDao;
import kr.co.mdi.common.util.MethodNameUtil;
import kr.co.mdi.cpu.dao.CpuDao;
import kr.co.mdi.cpu.dto.CoreStatDTO;
import kr.co.mdi.cpu.dto.CpuDTO;

@Profile("dev-user-mysql")
@Repository
public class CpuDaoImpl extends AbstractJdbcDao implements CpuDao {

	@Override
	public int selectTotalCpuCount() {
		String sql = "SELECT COUNT(*) FROM cpu";
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
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
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}

		return cpuList;
	}

	@Override
	public CpuDTO selectCpuById(Integer cpuId) {
//	public CpuDTO selectCpuById(Integer cpuId, int appUserId) {
		
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
		
//	    try (Connection conn = getConnection()) {
//	    	setAppUserId(conn, appUserId);
//	    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
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
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}

		return cpu;
	} 

	@Override
	public CpuDTO selectCpuByName(String nameCpu) {
		String sql = "SELECT * FROM cpu WHERE name_cpu = ?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return null;
	}

	@Override
	public List<CpuDTO> selectCpuListByName(String nameCpu) {
		List<CpuDTO> cpuList = new ArrayList<>();

		String sql = """
				    SELECT
				        c.id_cpu,
				        c.name_cpu,
				        c.core_cpu,
				        c.thread_cpu,
				        c.maxghz_cpu,
				        c.minghz_cpu,
				        b.manf_cpu
				    FROM cpu c
				    LEFT JOIN cpu_manf_brand b ON c.cpu_manf_code = b.cpu_manf_code
				    WHERE c.name_cpu LIKE ?
				    ORDER BY c.id_cpu ASC
				""";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, "%" + nameCpu + "%");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				CpuDTO dto = new CpuDTO();
				dto.setIdCpu(rs.getInt("id_cpu"));
				dto.setNameCpu(rs.getString("name_cpu"));
				dto.setCoreCpu(rs.getInt("core_cpu"));
				dto.setThreadCpu(rs.getInt("thread_cpu"));
				dto.setMaxghzCpu(rs.getFloat("maxghz_cpu"));
				dto.setMinghzCpu(rs.getFloat("minghz_cpu"));
				dto.setManfCpu(rs.getString("manf_cpu")); // 제조사 이름
				cpuList.add(dto);
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return cpuList;
	}

	// -----------------------------------------

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
				    WHERE m.choice_cpu > 0
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
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return hotCpuList;
	}

	public List<CpuDTO> selectCpuListByManufacturer(String manfCpu) {
		List<CpuDTO> list = new ArrayList<>();
		String sql = """
				    SELECT c.*, b.manf_cpu
				    FROM cpu c
				    JOIN cpu_manf_brand b ON c.cpu_manf_code = b.cpu_manf_code
				    WHERE b.manf_cpu = ?
				""";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, manfCpu);

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
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return list;
	}

	@Override
	public List<CpuDTO> selectCpuListByCore(int coreCpu) {
		List<CpuDTO> list = new ArrayList<>();
		String sql = """
				    SELECT c.*, b.manf_cpu
				    FROM cpu c
				    JOIN cpu_manf_brand b ON c.cpu_manf_code = b.cpu_manf_code
				    WHERE c.core_cpu = ?
				""";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
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

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
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

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return list;
	}

	@Override
	public List<CoreStatDTO> getCoreCpuDistribution() {
		List<CoreStatDTO> list = new ArrayList<>();
		String sql = """
				    SELECT core_cpu, COUNT(*) AS count
				    FROM cpu
				    GROUP BY core_cpu
				    ORDER BY core_cpu
				""";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				CoreStatDTO stat = new CoreStatDTO();
				stat.setCoreCpu(rs.getInt("core_cpu"));
				stat.setCount(rs.getInt("count"));
				list.add(stat);
			}
			System.out.println("코어 분포:");
			for (CoreStatDTO stat : list) {
				System.out.println(stat.getCoreCpu() + "코어: " + stat.getCount());
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return list;
	}

}
