package kr.co.mdi.common.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * DBMS 중립<br>
 * postgreQL에서는 getConnection() 오버라이드<br>
 */
public abstract class AbstractJdbcDao {
	@Autowired
	protected DataSource dataSource;

	protected Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
    protected void setAppUserId(Connection conn, int appUserId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SET @app_user_id = ?")) {
            ps.setInt(1, appUserId);
            ps.executeUpdate();
        }
    }
	
}

