package kr.co.mdi.device.dao.postgre;

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
import kr.co.mdi.device.dao.DeviceDao;
import kr.co.mdi.device.dto.DeviceDTO;
import kr.co.mdi.device.dto.ManfStatDTO;

@Profile("dev-user-postgre")
@Repository
public class DeviceDaoImpl extends AbstractJdbcDao implements DeviceDao {
	
//	@Value("${current.schema}")
//	private String currentSchema;

//    @Override
//	public Connection getConnection() throws SQLException {
//        Connection conn = dataSource.getConnection();
//        try (PreparedStatement stmt = conn.prepareStatement("SET search_path TO " + currentSchema)) {
//            stmt.execute();
//        }
//        return conn;
//    }
	
	private final DataSource dataSource;

	public DeviceDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}
    
    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection(); // SET search_path 생략
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
		} catch (SQLException e) {
			throw new RuntimeException("디바이스 row 수 조회 중 오류 발생", e);
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
			se.printStackTrace();
			throw new RuntimeException("DB 조회 중 오류 발생", se);
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
				        c.name_cpu
				    FROM device m
				    LEFT JOIN device_type t ON m.device_type_code = t.device_type_code
				    LEFT JOIN device_manf_brand b ON m.device_manf_code = b.device_manf_code
				    LEFT JOIN cpu c ON m.id_cpu = c.id_cpu
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
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new RuntimeException("DB 조회 중 오류 발생", se);
		}

		return device;
	}
	
	// ------
	
	@Override
	public List<DeviceDTO> selectDevicesByCpuName(String cpuName) {
	    String sql = "SELECT * FROM device WHERE cpu_device = ? ORDER BY id_device DESC";
	    List<DeviceDTO> list = new ArrayList<>();

	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, cpuName);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                DeviceDTO dto = new DeviceDTO();
	                dto.setIdDevice(rs.getInt("id_device"));
	                dto.setNameDevice(rs.getString("name_device"));
	                dto.setManfDevice(rs.getString("manf_device"));
	                dto.setCpuDevice(rs.getString("cpu_device"));
	                dto.setReleaseDevice(rs.getInt("release_device"));
	                list.add(dto);
	            }
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("디바이스 조회 실패", e);
	    }

	    return list;
	}
	
	// ------
	
//	@Override
//	public Map<String, Integer> selectDeviceCountByBrand(String cpuName) {
//	    String sql = "SELECT manf_device, COUNT(*) AS count FROM mdl WHERE cpu_device = ? GROUP BY manf_device";
//	    return jdbcTemplate.query(sql, new Object[]{cpuName}, rs -> {
//	        Map<String, Integer> result = new LinkedHashMap<>();
//	        while (rs.next()) {
//	            result.put(rs.getString("manf_device"), rs.getInt("count"));
//	        }
//	        return result;
//	    });
//	}


	@Override
	public Map<String, Integer> selectDeviceCountByBrand(String cpuName) {
	    String sql = """
	        SELECT b.manf_device, COUNT(*) AS count
	        FROM device m
	        LEFT JOIN device_manf_brand b ON m.device_manf_code = b.device_manf_code
	        WHERE m.cpu_device = ?
	        GROUP BY b.manf_device
	    """;

	    Map<String, Integer> brandCountMap = new LinkedHashMap<>();

	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, cpuName);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                String brand = rs.getString("manf_device");
	                int count = rs.getInt("count");
	                brandCountMap.put(brand, count);
	            }
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException("제조사별 디바이스 수 집계 중 오류 발생", e);
	    }

	    return brandCountMap;
	}
	
	@Override
	public List<DeviceDTO> selectDeviceListByName(String nameDevice) {
	    List<DeviceDTO> list = new ArrayList<>();
	    String sql = "SELECT * FROM device WHERE name_device LIKE ? ORDER BY id_device ASC";

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, "%" + nameDevice + "%");
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            DeviceDTO dto = new DeviceDTO();
	            dto.setIdDevice(rs.getInt("id_device"));
	            dto.setNameDevice(rs.getString("name_device"));
	            dto.setLineupDevice(rs.getString("lineup_device"));
	            dto.setReleaseDevice(rs.getInt("release_device"));
	            dto.setWeightDevice(rs.getFloat("weight_device"));
	            dto.setChoiceDevice(rs.getInt("choice_device"));
	            dto.setTypeDevice(rs.getString("type_device"));
	            dto.setManfDevice(rs.getString("manf_device"));
	            dto.setIdCpu(rs.getInt("id_cpu"));
	            list.add(dto);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
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
	            c.manf_cpu AS cpu_manf
	        FROM mdl d
	        LEFT JOIN device_manf_brand b ON d.device_manf_code = b.device_manf_code
	        LEFT JOIN mcl c ON d.id_cpu = c.id_cpu
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

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("인기 디바이스 목록 조회 중 오류 발생", e);
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

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

	    } catch (SQLException e) {
	        e.printStackTrace();
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

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
//	@Override
//	public List<ManfStatDTO> getDeviceManufacturerDistribution() {
//	    List<ManfStatDTO> list = new ArrayList<>();
//	    String sql = """
//	        SELECT manf_device, COUNT(*) AS count
//	        FROM device
//	        GROUP BY manf_device
//	        ORDER BY manf_device
//	    """;
//
//	    try (Connection conn = dataSource.getConnection();
//	         PreparedStatement pstmt = conn.prepareStatement(sql);
//	         ResultSet rs = pstmt.executeQuery()) {
//
//	        while (rs.next()) {
//	            ManfStatDTO stat = new ManfStatDTO();
//	            stat.setManfDevice(rs.getString("manf_device"));
//	            stat.setCount(rs.getInt("count"));
//	            list.add(stat);
//	        }
//
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//
//	    return list;
//	}
	
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

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            ManfStatDTO stat = new ManfStatDTO();
	            stat.setManfDevice(rs.getString("manf_device"));
	            stat.setCount(rs.getInt("count"));
	            list.add(stat);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
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

	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
	        se.printStackTrace();
	        throw new RuntimeException("기기 유형별 디바이스 조회 중 오류 발생", se);
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

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("출시년도별 디바이스 조회 중 오류 발생", e);
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

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("CPU + 제조사 조건으로 디바이스 조회 중 오류 발생", e);
	    }

	    return list;
	}

}
