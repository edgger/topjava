package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JDBC)
public class MealServiceTestingJDBCTest extends MealServiceTesting {

    @Override
    public void getWithUser() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        super.getWithUser();
    }
}
