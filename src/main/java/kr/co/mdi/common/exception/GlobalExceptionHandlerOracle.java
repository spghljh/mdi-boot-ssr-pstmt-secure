package kr.co.mdi.common.exception;

import java.sql.SQLException;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Profile("dev-user-oracle")
@ControllerAdvice
public class GlobalExceptionHandlerOracle {

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
	public String handleOracleSqlError(SQLException e, Model model) {
		int errorCode = e.getErrorCode();

		switch (errorCode) {
		case 942: // ORA-00942
			model.addAttribute("errorMessage", "요청하신 테이블이 존재하지 않습니다.");
			break;
		case 904: // ORA-00904
			model.addAttribute("errorMessage", "잘못된 컬럼명이 포함되어 있습니다.");
			break;
		case 936: // ORA-00936
		case 933: // ORA-00933
			model.addAttribute("errorMessage", "SQL 문법 오류");
			break;
		case 1031: // ORA-01031
			model.addAttribute("errorMessage", "권한 부족.");
			break;
		case 1017: // ORA-01017
			model.addAttribute("errorMessage", "데이터베이스 인증 실패");
			break;
		case 1: // ORA-00001
			model.addAttribute("errorMessage", "중복 데이터 존재.");
			break;
		default:
			model.addAttribute("errorMessage", "Oracle 데이터 처리 중 오류");
		}

		return "error/db-error";
	}

}
