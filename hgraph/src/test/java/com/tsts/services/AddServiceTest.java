package com.tsts.services;

import com.tsts.services.utils.Utils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.File;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddServiceTest {
    private AddService addService;
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
        addService = new AddService();
        mockedUtils.reset();
    }

    @Test
    void testAddRepositoryWithNoGit() {
        mockedUtils.when(() -> Utils.scanGitRepositories(any())).thenReturn(Collections.emptyList());

        addService.add("/home/tayssir/projects/pico-hello/");

        mockedUtils.verify(() -> Utils.saveRepositories(any()), never());
        mockedUtils.verify(() -> Utils.scanGitRepositories(any()), times(1));
    }


    @Test
    void testAddRepositoryWithNoPath() {
        addService.add("");

        mockedUtils.verify(() -> Utils.scanGitRepositories(any()), never());
        mockedUtils.verify(() -> Utils.saveRepositories(any()), never());
    }

    @Test
    void testAddRepositoryWithInvalidPath() {
        File invalidFolder = mock(File.class);
        when(invalidFolder.exists()).thenReturn(false);

        addService.add("invalid/path");

        mockedUtils.verify(() -> Utils.scanGitRepositories(any()), never());
        mockedUtils.verify(() -> Utils.saveRepositories(any()), never());
    }

    @Test
    void testAddRepositorySuccessfully() {
        mockedUtils.when(() -> Utils.scanGitRepositories(any())).thenReturn(Collections.singletonList("/home/tayssir/projects/tsts-git/"));
        mockedUtils.when(Utils::loadRepositories).thenReturn(Collections.emptyList());

        addService.add("/home/tayssir/projects/tsts-git/");

        mockedUtils.verify(() -> Utils.scanGitRepositories(any()), times(1));
        mockedUtils.verify(() -> Utils.saveRepositories(Collections.singletonList("/home/tayssir/projects/tsts-git/")), times(1));
    }

}