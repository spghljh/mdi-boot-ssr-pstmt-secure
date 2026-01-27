package kr.co.mdi.common.exception;

import java.sql.SQLException;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Profile("dev-user-mysql")
@ControllerAdvice
public class GlobalExceptionHandlerMysql {

	@ExceptionHandler(CannotGetJdbcConnectionException.class)
	public String handleJdbcConnectionError(Model model) {
		model.addAttribute("errorMessage", "데이터베이스 연결에 실패했습니다.");
		return "error/db-error";
	}

	@ExceptionHandler(DataAccessException.class)
	public String handleDataAccessError(Model model) {
		model.addAttribute("errorMessage", "데이터 처리 중 오류가 발생했습니다.");
		return "error/db-error";
	}

    @ExceptionHandler(SQLException.class)
    public String handleMysqlSqlError(SQLException e, Model model) {
        int errorCode = e.getErrorCode();

        switch (errorCode) {
        case 1049: // Unknown database
            model.addAttribute("errorMessage", "요청하신 데이터베이스가 존재하지 않습니다.");
            break;
        case 1146: // Table doesn't exist
            model.addAttribute("errorMessage", "요청하신 테이블이 존재하지 않습니다.");
            break;
        case 1054: // Unknown column
            model.addAttribute("errorMessage", "잘못된 컬럼명이 포함되어 있습니다.");
            break;
        case 1064: // SQL syntax error
            model.addAttribute("errorMessage", "SQL 문법 오류가 발생했습니다.");
            break;
        case 1062: // Duplicate entry
            model.addAttribute("errorMessage", "중복된 데이터가 존재합니다.");
            break;
        case 1045: // Access denied
            model.addAttribute("errorMessage", "데이터베이스 접근 권한이 없습니다.");
            break;
        default:
            model.addAttribute("errorMessage", "MySQL 데이터 처리 중 오류가 발생했습니다.");
        }

        return "error/db-error";
    }

}
