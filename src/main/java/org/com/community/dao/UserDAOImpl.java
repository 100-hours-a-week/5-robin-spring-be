package org.com.community.dao;

import org.com.community.domain.User;
import org.com.community.dto.UserDTO;
import org.com.community.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("id"))
                    .email(rs.getString("email"))
                    .pwd(rs.getString("pwd"))
                    .nickname(rs.getString("nickname"))
                    .profilePath(rs.getString("profile_path"))
                    .createAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .delFlag(rs.getInt("del_flag"))
                    .build();

        }
    }

    @Override
    public Long addUser(User user) {
        String sql = "INSERT INTO users (email, pwd, nickname, profile_path) values (?, ? ,?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getPwd(), user.getNickname(), user.getProfilePath());
        String getIdSql = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(getIdSql, Long.class);
    }


    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        String sql = "SELECT COUNT(*) FROM users WHERE nickname = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nickname);
        return count != null && count > 0;
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND del_flag = 0";
        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
        return user;
    }

    @Override
    public Long updateUser(User user) {
        String sql = "UPDATE users SET nickname = ?, profile_path = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getNickname(), user.getProfilePath(), user.getId());
        String getIdSql = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(getIdSql, Long.class);
    }

    @Override
    public Long updatePassword(User user) {
        String sql = "UPDATE users SET pwd = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getPwd(), user.getId());
        String getIdSql = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(getIdSql, Long.class);
    }

    @Override
    public Long deleteUser(User user) {
        String sql = "UPDATE users SET del_flag = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getDelFlag(), user.getId());
        String getIdSql = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(getIdSql, Long.class);
    }
}
