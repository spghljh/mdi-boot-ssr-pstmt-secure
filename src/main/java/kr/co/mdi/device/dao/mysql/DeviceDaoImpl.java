package kr.co.mdi.device.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import kr.co.mdi.common.jdbc.AbstractJdbcDao;
import kr.co.mdi.common.util.MethodNameUtil;
import kr.co.mdi.device.dao.DeviceDao;
import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.device.dto.ManfStatDTO;

@Profile("dev-user-mysql")
@Repository
public class DeviceDaoImpl extends AbstractJdbcDao implements DeviceDao {

	private final DataSource dataSource;

	public DeviceDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection(); // 커넥션 풀에서 가져옴
	}

	@Override
	public int selectTotalDeviceCount() {
		String sql = "SELECT COUNT(*) FROM device";
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
	public List<DeviceDTO> selectAllDevices() {
		List<DeviceDTO> deviceList = new ArrayList<>();
		String sql = """
				    SELECT
				        m.id_device,
				        m.name_device,
				        m.lineup_device,
				        m.release_device,
				        m.weight_device,
				        m.choice_device,
				        m.device_type_code,
				        t.type_device,
				        m.device_manf_code,
				        b.manf_device,
				        m.id_cpu,
				        c.name_cpu
				    FROM device m
				    LEFT JOIN device_type t ON m.device_type_code = t.device_type_code
				    LEFT JOIN device_manf_brand b ON m.device_manf_code = b.device_manf_code
				    LEFT JOIN cpu c ON m.id_cpu = c.id_cpu
				""";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				DeviceDTO device = new DeviceDTO();
				device.setIdDevice(rs.getInt("id_device"));
				device.setNameDevice(rs.getString("name_device"));
				device.setLineupDevice(rs.getString("lineup_device"));
				device.setReleaseDevice(rs.getInt("release_device"));
				device.setWeightDevice(rs.getFloat("weight_device"));
				device.setChoiceDevice(rs.getInt("choice_device"));
				device.setDeviceTypeCode(rs.getString("device_type_code"));
				device.setTypeDevice(rs.getString("type_device"));
				device.setDeviceManfCode(rs.getString("device_manf_code"));
				device.setManfDevice(rs.getString("manf_device"));
				device.setIdCpu(rs.getInt("id_cpu"));
				device.setCpuDevice(rs.getString("name_cpu"));
				deviceList.add(device);
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return deviceList;
	}

	@Override
	public DeviceDTO selectDeviceById(Integer deviceId) {
		String sql = """
				    SELECT
				        m.id_device,
				        m.name_device,
				        m.lineup_device,
				        m.release_device,
				        m.weight_device,
				        m.choice_device,
				        m.device_type_code,
				        t.type_device,
				        m.device_manf_code,
				        b.manf_device,
				        m.id_cpu,
				        c.name_cpu,
				        cb.manf_cpu
				    FROM device m
				    LEFT JOIN device_type t ON m.device_type_code = t.device_type_code
				    LEFT JOIN device_manf_brand b ON m.device_manf_code = b.device_manf_code
				    LEFT JOIN cpu c ON m.id_cpu = c.id_cpu
				    LEFT JOIN cpu_manf_brand cb ON c.cpu_manf_code = cb.cpu_manf_code
				    WHERE m.id_device = ?
				""";

		DeviceDTO device = null;

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, deviceId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					device = new DeviceDTO();
					device.setIdDevice(rs.getInt("id_device"));
					device.setNameDevice(rs.getString("name_device"));
					device.setLineupDevice(rs.getString("lineup_device"));
					device.setReleaseDevice(rs.getInt("release_device"));
					device.setWeightDevice(rs.getFloat("weight_device"));
					device.setChoiceDevice(rs.getInt("choice_device"));
					device.setDeviceTypeCode(rs.getString("device_type_code"));
					device.setTypeDevice(rs.getString("type_device"));
					device.setDeviceManfCode(rs.getString("device_manf_code"));
					device.setManfDevice(rs.getString("manf_device"));
					device.setIdCpu(rs.getInt("id_cpu"));
					device.setCpuDevice(rs.getString("name_cpu"));
					device.setManfCpu(rs.getString("manf_cpu"));
					System.out.println("device_type_code = " + rs.getString("device_type_code"));
				}
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return device;
	}

	@Override
	public List<DeviceDTO> selectDevicesByCpuName(String cpuName) {
		String sql = """
				    SELECT
				        d.id_device,
				        d.name_device,
				        d.release_device,
				        d.device_manf_code,
				        b.manf_device,
				        d.id_cpu,
				        c.name_cpu AS cpu_device
				    FROM device d
				    LEFT JOIN cpu c ON d.id_cpu = c.id_cpu
				    LEFT JOIN device_manf_brand b ON d.device_manf_code = b.device_manf_code
				    WHERE c.name_cpu = ?
				    ORDER BY d.id_device DESC
				""";

		List<DeviceDTO> list = new ArrayList<>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, cpuName);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					DeviceDTO dto = new DeviceDTO();
					dto.setIdDevice(rs.getInt("id_device"));
					dto.setNameDevice(rs.getString("name_device"));
					dto.setReleaseDevice(rs.getInt("release_device"));
					dto.setManfDevice(rs.getString("manf_device"));
					dto.setCpuDevice(rs.getString("cpu_device"));
					list.add(dto);
				}
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return list;
	}

	@Override
	public Map<String, Integer> selectDeviceCountByBrand(String cpuName) {
		String sql = """
				    SELECT b.manf_device, COUNT(*) AS count
				    FROM device m
				    LEFT JOIN device_manf_brand b ON m.device_manf_code = b.device_manf_code
				    LEFT JOIN cpu c ON m.id_cpu = c.id_cpu
				    WHERE c.name_cpu = ?
				    GROUP BY b.manf_device
				""";

		Map<String, Integer> brandCountMap = new LinkedHashMap<>();

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, cpuName);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String brand = rs.getString("manf_device");
					int count = rs.getInt("count");
					brandCountMap.put(brand, count);
				}
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return brandCountMap;
	}

	@Override
	public List<DeviceDTO> selectDeviceListByName(String nameDevice) {
		List<DeviceDTO> deviceList = new ArrayList<>();

		String sql = """
				SELECT
				        d.id_device,
				        d.name_device,
				        d.id_cpu,
				        b.manf_device,
				        t.type_device,
				        c.name_cpu AS cpu_device
				    FROM device d
				    LEFT JOIN device_manf_brand b ON d.device_manf_code = b.device_manf_code
				    LEFT JOIN device_type t ON d.device_type_code = t.device_type_code
				    LEFT JOIN cpu c ON d.id_cpu = c.id_cpu
				    WHERE d.name_device LIKE ?
				    ORDER BY d.id_device ASC
				 """;

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, "%" + nameDevice + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				DeviceDTO dto = new DeviceDTO();
				dto.setIdDevice(rs.getInt("id_device"));
				dto.setNameDevice(rs.getString("name_device"));
				dto.setIdCpu(rs.getInt("id_cpu"));
				dto.setManfDevice(rs.getString("manf_device"));
				dto.setTypeDevice(rs.getString("type_device"));
				dto.setCpuDevice(rs.getString("cpu_device"));
				deviceList.add(dto);
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return deviceList;
	}

	@Override
	public List<DeviceDTO> selectHotDeviceList() {
		List<DeviceDTO> hotDeviceList = new ArrayList<>();

		String sql = """
				    SELECT
				        d.id_device,
				        d.name_device,
				        d.choice_device,
				        d.device_manf_code,
				        b.manf_device,
				        d.id_cpu,
				        c.name_cpu AS cpu_name,
				        cb.manf_cpu AS cpu_manf
				    FROM device d
				    LEFT JOIN device_manf_brand b ON d.device_manf_code = b.device_manf_code
				    LEFT JOIN cpu c ON d.id_cpu = c.id_cpu
				    LEFT JOIN cpu_manf_brand cb ON c.cpu_manf_code = cb.cpu_manf_code
				    WHERE d.choice_device > 0
				    ORDER BY d.choice_device DESC
				    LIMIT 10
				""";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				DeviceDTO dto = new DeviceDTO();
				dto.setIdDevice(rs.getInt("id_device"));
				dto.setNameDevice(rs.getString("name_device"));
				dto.setChoiceDevice(rs.getInt("choice_device"));
				dto.setDeviceManfCode(rs.getString("device_manf_code"));
				dto.setManfDevice(rs.getString("manf_device"));
				dto.setIdCpu(rs.getInt("id_cpu"));
				dto.setCpuDevice(rs.getString("cpu_name"));
				dto.setManfCpu(rs.getString("cpu_manf"));
				hotDeviceList.add(dto);
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return hotDeviceList;
	}

	@Override
	public List<DeviceDTO> selectDeviceListByManufacturer(String manfDevice) {
		List<DeviceDTO> list = new ArrayList<>();
		String sql = """
				    SELECT d.*, b.manf_device
				    FROM device d
				    JOIN device_manf_brand b ON d.device_manf_code = b.device_manf_code
				    WHERE b.manf_device = ?
				""";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, manfDevice);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					DeviceDTO device = new DeviceDTO();
					device.setIdDevice(rs.getInt("id_device"));
					device.setNameDevice(rs.getString("name_device"));
					device.setIdCpu(rs.getInt("id_cpu"));
					device.setLineupDevice(rs.getString("lineup_device"));
					device.setReleaseDevice(rs.getInt("release_device"));
					device.setWeightDevice(rs.getFloat("weight_device"));
					device.setChoiceDevice(rs.getInt("choice_device"));
					device.setDeviceTypeCode(rs.getString("device_type_code"));
					device.setDeviceManfCode(rs.getString("device_manf_code"));
					device.setManfDevice(rs.getString("manf_device")); // 제조사 이름 세팅
					list.add(device);
				}
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return list;
	}

	@Override
	public List<DeviceDTO> selectDeviceListByCpuId(int idCpu) {
		List<DeviceDTO> list = new ArrayList<>();

		String sql = """
				    SELECT d.*, b.manf_device, c.name_cpu
				    FROM device d
				    JOIN device_manf_brand b ON d.device_manf_code = b.device_manf_code
				    JOIN cpu c ON d.id_cpu = c.id_cpu
				    WHERE d.id_cpu = ?
				""";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, idCpu);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					DeviceDTO device = new DeviceDTO();
					device.setIdDevice(rs.getInt("id_device"));
					device.setNameDevice(rs.getString("name_device"));
					device.setCpuDevice(rs.getString("name_cpu")); // CPU 이름
					device.setManfDevice(rs.getString("manf_device")); // 제조사 이름
					device.setDeviceTypeCode(rs.getString("device_type_code"));
					device.setDeviceManfCode(rs.getString("device_manf_code"));
					device.setIdCpu(rs.getInt("id_cpu"));
					device.setChoiceDevice(rs.getInt("choice_device"));
					list.add(device);
				}
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return list;
	}

	@Override
	public List<ManfStatDTO> getDeviceManufacturerDistribution() {
		List<ManfStatDTO> list = new ArrayList<>();
		String sql = """
				    SELECT b.manf_device, COUNT(*) AS count
				    FROM device d
				    JOIN device_manf_brand b ON d.device_manf_code = b.device_manf_code
				    GROUP BY b.manf_device
				    ORDER BY b.manf_device
				""";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				ManfStatDTO stat = new ManfStatDTO();
				stat.setManfDevice(rs.getString("manf_device"));
				stat.setCount(rs.getInt("count"));
				list.add(stat);
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return list;
	}

	@Override
	public List<DeviceDTO> selectDevicesByTypeCode(String typeCode) {
		List<DeviceDTO> deviceList = new ArrayList<>();
		String sql = """
				    SELECT
				        m.id_device,
				        m.name_device,
				        m.lineup_device,
				        m.release_device,
				        m.weight_device,
				        m.choice_device,
				        m.device_type_code,
				        t.type_device,
				        m.device_manf_code,
				        b.manf_device,
				        m.id_cpu,
				        c.name_cpu
				    FROM device m
				    LEFT JOIN device_type t ON m.device_type_code = t.device_type_code
				    LEFT JOIN device_manf_brand b ON m.device_manf_code = b.device_manf_code
				    LEFT JOIN cpu c ON m.id_cpu = c.id_cpu
				    WHERE m.device_type_code = ?
				""";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, typeCode);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					DeviceDTO device = new DeviceDTO();
					device.setIdDevice(rs.getInt("id_device"));
					device.setNameDevice(rs.getString("name_device"));
					device.setLineupDevice(rs.getString("lineup_device"));
					device.setReleaseDevice(rs.getInt("release_device"));
					device.setWeightDevice(rs.getFloat("weight_device"));
					device.setChoiceDevice(rs.getInt("choice_device"));
					device.setDeviceTypeCode(rs.getString("device_type_code"));
					device.setTypeDevice(rs.getString("type_device"));
					device.setDeviceManfCode(rs.getString("device_manf_code"));
					device.setManfDevice(rs.getString("manf_device"));
					device.setIdCpu(rs.getInt("id_cpu"));
					device.setCpuDevice(rs.getString("name_cpu"));
					deviceList.add(device);
				}
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return deviceList;
	}

	@Override
	public List<DeviceDTO> selectDeviceListByReleaseYear(int releaseYear) {
		List<DeviceDTO> list = new ArrayList<>();
		String sql = """
				    SELECT
				        m.id_device,
				        m.name_device,
				        m.lineup_device,
				        m.release_device,
				        m.weight_device,
				        m.choice_device,
				        m.device_type_code,
				        t.type_device,
				        m.device_manf_code,
				        b.manf_device,
				        m.id_cpu,
				        c.name_cpu
				    FROM device m
				    LEFT JOIN device_type t ON m.device_type_code = t.device_type_code
				    LEFT JOIN device_manf_brand b ON m.device_manf_code = b.device_manf_code
				    LEFT JOIN cpu c ON m.id_cpu = c.id_cpu
				    WHERE m.release_device = ?
				""";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, releaseYear);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					DeviceDTO dto = new DeviceDTO();
					dto.setIdDevice(rs.getInt("id_device"));
					dto.setNameDevice(rs.getString("name_device"));
					dto.setLineupDevice(rs.getString("lineup_device"));
					dto.setReleaseDevice(rs.getInt("release_device"));
					dto.setWeightDevice(rs.getFloat("weight_device"));
					dto.setChoiceDevice(rs.getInt("choice_device"));
					dto.setDeviceTypeCode(rs.getString("device_type_code"));
					dto.setTypeDevice(rs.getString("type_device"));
					dto.setDeviceManfCode(rs.getString("device_manf_code"));
					dto.setManfDevice(rs.getString("manf_device"));
					dto.setIdCpu(rs.getInt("id_cpu"));
					dto.setCpuDevice(rs.getString("name_cpu"));
					list.add(dto);

				}
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return list;
	}

	@Override
	public List<DeviceDTO> selectDeviceListByCpuAndManf(int idCpu, String manfDevice) {
		List<DeviceDTO> list = new ArrayList<>();

		String sql = """
				    SELECT d.id_device,
				           d.name_device,
				           d.lineup_device,
				           d.release_device,
				           d.weight_device,
				           d.choice_device,
				           d.device_type_code,
				           t.type_device,
				           d.device_manf_code,
				           b.manf_device,
				           d.id_cpu,
				           c.name_cpu
				    FROM device d
				    LEFT JOIN device_type t ON d.device_type_code = t.device_type_code
				    LEFT JOIN device_manf_brand b ON d.device_manf_code = b.device_manf_code
				    LEFT JOIN cpu c ON d.id_cpu = c.id_cpu
				    WHERE d.id_cpu = ? AND b.manf_device = ?
				""";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, idCpu);
			pstmt.setString(2, manfDevice);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					DeviceDTO dto = new DeviceDTO();
					dto.setIdDevice(rs.getInt("id_device"));
					dto.setNameDevice(rs.getString("name_device"));
					dto.setLineupDevice(rs.getString("lineup_device"));
					dto.setReleaseDevice(rs.getInt("release_device"));
					dto.setWeightDevice(rs.getFloat("weight_device"));
					dto.setChoiceDevice(rs.getInt("choice_device"));
					dto.setDeviceTypeCode(rs.getString("device_type_code"));
					dto.setTypeDevice(rs.getString("type_device"));
					dto.setDeviceManfCode(rs.getString("device_manf_code"));
					dto.setManfDevice(rs.getString("manf_device"));
					dto.setIdCpu(rs.getInt("id_cpu"));
					dto.setCpuDevice(rs.getString("name_cpu"));
					list.add(dto);
				}
			}
		} catch (SQLException se) {
			throw new RuntimeException(MethodNameUtil.getCurrentMethodName(), se);
		}
		return list;
	}

}
