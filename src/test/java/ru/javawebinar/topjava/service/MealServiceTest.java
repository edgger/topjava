package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.ADMIN_MEALS;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL;
import static ru.javawebinar.topjava.MealTestData.USER_MEALS;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Before
    public void before(){
        USER_MEALS.addAll(service.getAll(AbstractBaseEntity.START_SEQ));
        ADMIN_MEALS.addAll(service.getAll(AbstractBaseEntity.START_SEQ+1));
    }

    @Test
    public void get() {
        Meal meal = service.get(100002, 100000);
        assertThat(meal).isEqualToComparingFieldByField(USER_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundGet() {
        Meal meal = service.get(100003, 100001);
    }

    @Test
    public void delete() {
        service.delete(100002,100000);
        assertThat(service.getAll(100000)).doesNotContain(USER_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundDelete() {
        service.delete(100008,100000);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> mealsBetweenDates = service.getBetweenDates(LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 16), 100001);
        assertThat(mealsBetweenDates.size()).isEqualTo(3);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> mealsBetweenDates = service.getBetweenDateTimes(LocalDateTime.of(2018, Month.JUNE, 16, 20, 0),
                LocalDateTime.of(2018, Month.JUNE, 17, 11 , 0), 100001);
        assertThat(mealsBetweenDates.size()).isEqualTo(2);
    }

    @Test
    public void getAll() {
        assertThat(service.getAll(100001).size()).isEqualTo(6);
    }

    @Test
    public void update() {
        Meal meal = service.get(100003, 100000);
        meal.setCalories(100);
        service.update(meal, 100000);
        assertThat(service.get(100003, 100000).getCalories()).isEqualToComparingFieldByField(100);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundUpdate() {
        Meal meal = service.get(100003, 100000);
        meal.setCalories(100);
        service.update(meal, 100001);
    }

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.of(2018, Month.MAY, 18, 10, 36, 38), "трак", 2000);
        Meal mealCreated = service.create(meal, 100001);
        meal.setId(mealCreated.getId());
        assertThat(meal).isEqualToComparingFieldByField(mealCreated);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createDuplicate() {
        Meal meal = new Meal(LocalDateTime.of(2018, Month.MAY, 18, 10, 36, 38), "трак", 2000);
        Meal meal2 = new Meal(LocalDateTime.of(2018, Month.MAY, 18, 10, 36, 38), "трак3", 300);
        Meal mealCreated = service.create(meal, 100001);
        Meal mealCreated2 = service.create(meal2, 100001);
    }
}