package kr.co.mdi.member.dao;

public interface SequenceBasedMemberDao {

	/**
	 * -시퀀스를 통해 다음 회원 ID를 가져옵니다.<br>
	 * Oracle: SELECT seq_id_member.NEXTVAL FROM dual<br>
	 * postgreQL: SELECT nextval('seq_id_member')<br>
	 */
	int getNextMemberId();

}
