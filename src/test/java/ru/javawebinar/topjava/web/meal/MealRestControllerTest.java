package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Test
    public void testGetAll() throws Exception {
        TestUtil.print(mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(MEALS))));
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(MealRestController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(MEALS)));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(AuthorizedUser.id()), MEAL6,MEAL5,MEAL4,MEAL3,MEAL2);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setCalories(350);
        updated.setDescription("updated");

        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());

        assertMatch(mealService.get(MEAL1_ID, AuthorizedUser.id()), updated);
    }

    @Test
    public void testCreateWithLocation() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Meal expected = new Meal(now, "now test create", 570);

        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = TestUtil.readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(mealService.getAll(AuthorizedUser.id()), expected,MEAL6,MEAL5,MEAL4,MEAL3,MEAL2, MEAL1);
    }

    @Test
    public void testGetBetween() throws Exception {
        TestUtil.print(mockMvc.perform(get(REST_URL + "by?startDate=2015-05-30&startTime=10:15:30&endDate=2015-05-30&endTime=22:15:30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(Arrays.asList(MEAL2,MEAL3)))));
    }

    @Test
    public void testGetBetweenWithOnlyStart() throws Exception {
        TestUtil.print(mockMvc.perform(get(REST_URL + "by?startTime=15:15:30&startDate=2015-05-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(Arrays.asList(MEAL3,MEAL6)))));
    }
}