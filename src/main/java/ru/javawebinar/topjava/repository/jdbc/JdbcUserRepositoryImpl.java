package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        saveUserRoles(user);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User result = DataAccessUtils.singleResult(users);
        if (result!=null){
            Map<Integer, Set<Role>> roles = getRoles(result.getId());
            result.setRoles(roles.values().iterator().next());
        }
        return result;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User result = DataAccessUtils.singleResult(users);
        if (result!=null){
            Map<Integer, Set<Role>> roles = getRoles(result.getId());
            result.setRoles(roles.values().iterator().next());
        }
        return result;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> roles = jdbcTemplate.query("SELECT * FROM user_roles", rs -> {
            Map<Integer, Set<Role>> mapUserIdRole = new HashMap<>();

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                Role role = Role.valueOf(rs.getString("role"));
                mapUserIdRole.computeIfAbsent(userId, integer -> EnumSet.of(role)).add(role);
            }

            return mapUserIdRole;

        });
        users.forEach(user -> user.setRoles(roles.get(user.getId())));
        return users;
    }

    private Map<Integer, Set<Role>> getRoles(int userId){
        return jdbcTemplate.query("SELECT r.role FROM user_roles r WHERE r.user_id=?", rs -> {
            Map<Integer, Set<Role>> mapUserRoles = new HashMap<>();

            while (rs.next()) {
                Role role = Role.valueOf(rs.getString("role"));
                mapUserRoles.computeIfAbsent(userId, integer -> EnumSet.of(role)).add(role);
            }

            return mapUserRoles;

        }, userId);
    }

    private void saveUserRoles(User user){
        jdbcTemplate.update("DELETE FROM user_roles r WHERE r.user_id=?", user.getId());
        ArrayList<Role> roles = new ArrayList<>(user.getRoles());
        Integer id = user.getId();
        jdbcTemplate.batchUpdate("INSERT INTO user_roles(user_id, role) VALUES (?,? )", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1,id);
                ps.setString(2,roles.get(i).toString());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }
}
