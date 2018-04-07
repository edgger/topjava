package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JPA)
public class MealServiceTestingJPATest extends MealServiceTesting {

    @Override
    public void getWithUser() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        super.getWithUser();
    }
}
