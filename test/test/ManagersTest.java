package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import source.Managers;
import source.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
    }

    @Test
    void getDefaultHistory() {
    }

    @Test
    void mustCreateObject() {
        TaskManager taskManager = Managers.getDefault();
        Assertions.assertNotNull(taskManager, "Создался нулевой объект");
    }
}