package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Repository
@Profile("postgres")
public class JdbcMealRepositoryPostgresImpl extends JdbcMealRepositoryImpl<LocalDateTime> implements MealRepository {

    @Autowired
    public JdbcMealRepositoryPostgresImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(dataSource, jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    LocalDateTime getProperTime(LocalDateTime localDateTime) {
        return localDateTime;
    }
}
