package spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MemberDao {
	private JdbcTemplate jdbcTemplate;
	public MemberDao (DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
		// ds객체전달을 위해 xml파일에 의존주입 설정
	}
	//검색기능, 수정후 MemberRowMapper클래스를 불러오면 되기에 코드가 간결(All메서드도 동일)
	public Member selectByEmail(String email) {
		List<Member> result = jdbcTemplate.query(
				"select * from MEMBER where EMAIL = ?", new MemberRowMapper(),email);
		return result.isEmpty() ? null : result.get(0);
	}
	// 모든 정보 출력
	public Collection<Member> selectAll() {
		List<Member> result = jdbcTemplate.query(
				"select * from MEMBER", new MemberRowMapper());
		return result;
	}
	// 결과가 1행인 경우에 조회하는 메서드(1개의 행만 가지는 경우)
	public int count() {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from MEMBER", Integer.class);
									//RowMapper를 단일 객체로 변환해주는 타입이다.
									//queryForObject가 쿼리 결과를 하나의 행만 받는 메서드 이기에
									// 쿼리 결과가 1행 이상인 경우 query()메서드를 이용
		return count;
	}
	public void update(Member member) {
//		jdbcTemplate.update(
//				"update MEMBER set NAME=?, PASSWORD=? where EMAIL=?",
//				member.getName(), member.getPassword(), member.getEmail());
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) 
					throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(
						"update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?");
				pstmt.setString(1, member.getName());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getEmail());
				return pstmt;
			}
		});
	}
	public void insert(final Member member) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(
						"insert into MEMBER (ID, EMAIL, PASSWORD, NAME)"
						+ "values (MEMBER_SEQ.nextval, ?, ?, ?)", new String[] {"ID"});
				//인덱스 파라미터 설정
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				return pstmt;
			}
		}, keyHolder);
		Number keyValue = keyHolder.getKey();
		member.setId(keyValue.longValue());
	}
}