package kr.co.mdi.common.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

	// 암호화
	public static String encode(String rawPassword) {
		return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
	}

	// 비교
	public static boolean matches(String rawPassword, String encodedPassword) {
		return BCrypt.checkpw(rawPassword, encodedPassword);
	}
}
