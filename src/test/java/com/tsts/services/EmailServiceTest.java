package com.tsts.services;

import com.tsts.services.utils.Utils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;

class EmailServiceTest {
    private EmailService emailService;
    private static MockedStatic<Utils> mockedUtils;

    @BeforeAll
    static void setUpStaticMock() {
        mockedUtils = mockStatic(Utils.class);
    }
    @AfterAll
    static void tearDownStaticMock() {
        mockedUtils.close();
    }
    @BeforeEach
    void setUp() {
        emailService = new EmailService();
        mockedUtils.reset();
    }


    @Test
    void testShowActivityWithEmptyEmail() {
        emailService.showActivity("");

        mockedUtils.verify(Utils::loadRepositories, never());
    }

    @Test
    void testShowActivityWithNullEmail() {
        emailService.showActivity(null);
        mockedUtils.verify(Utils::loadRepositories, never());
    }
}
