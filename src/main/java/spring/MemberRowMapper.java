package spring;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

// 검색 기능에 중복되는 기능을 하나로 합침
public class MemberRowMapper implements RowMapper<Member>{
	@Override
	public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
		Member member = new Member(
					rs.getString("EMAIL"), 
					rs.getString("PASSWORD"), 
					rs.getString("NAME"), 
					rs.getTimestamp("REGDATE"));
		member.setId(rs.getLong("ID"));
		return member;
	}
}
