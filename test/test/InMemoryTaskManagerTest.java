package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import source.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;
    Task task1;
    Task task2;
    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;
    Epic epic2;
    Subtask subtask3;
    Subtask subtask4;
    Subtask subtask5;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        task1 = new Task("task1", "first task1");
        task2 = new Task("task2", "second task2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        epic1 = new Epic("epic1", "first epic1");
        taskManager.addEpic(epic1);

        subtask1 = new Subtask("subtask1", "first subtask1");
        subtask1.setEpicId(epic1.getId());
        taskManager.addSubTask(subtask1);

        subtask2 = new Subtask("subtask2", "second subtask2");
        subtask2.setEpicId(epic1.getId());
        taskManager.addSubTask(subtask2);

        epic2 = new Epic("epic2", "second epic2");
        taskManager.addEpic(epic2);

        subtask3 = new Subtask("subtask3", "first subtask3");
        subtask3.setEpicId(epic2.getId());
        subtask3.setStatus(StatusEnum.DONE);
        taskManager.addSubTask(subtask3);

        subtask4 = new Subtask("subtask4", "second subtask4");
        subtask4.setEpicId(epic2.getId());
        subtask4.setStatus(StatusEnum.IN_PROGRESS);
        taskManager.addSubTask(subtask4);

        subtask5 = new Subtask("subtask5", "third subtask5");
        subtask5.setEpicId(epic2.getId());
        subtask5.setStatus(StatusEnum.DONE);
        taskManager.addSubTask(subtask5);
    }

    @Test
    void taskShouldBeSame() {
        int one = task1.getId();
        task2.setId(one);
        Assertions.assertEquals(task1.getId(), task2.getId(), "не равны ID");
        Assertions.assertEquals(task1, task2, "Не равны объекты если равны ID");
    }

    @Test
    void epikShouldBeSame() {
        int one = epic1.getId();
        epic2.setId(one);
        Assertions.assertEquals(epic1.getId(), epic2.getId(), "не равны ID");
        Assertions.assertEquals(epic1, epic2, "Не равны объекты если равны ID");
    }

    @Test
    void addTasksDifferentTypes() {  //проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
        Task task10 = new Task("task10", "ten task10");
        taskManager.addTask(task10);
        Epic epic10 = new Epic("epic10", "ten epic10");
        taskManager.addEpic(epic10);
        Subtask subtask10 = new Subtask("subtask10", "ten subtask10");
        subtask10.setEpicId(epic10.getId());
        taskManager.addSubTask(subtask10);
        int task10Id = task10.getId();
        int epic10Id = epic10.getId();
        int subtask10Id = subtask10.getId();
        assertEquals(task10Id, taskManager.getTaskById(task10Id).getId(), "нет задачи с таким ID");
        assertEquals(epic10Id, taskManager.getEpicById(epic10Id).getId(), "нет эпика с таким ID");
        assertEquals(subtask10Id, taskManager.getSubTaskById(subtask10Id).getId(), "нет подзадачи с таким ID");
    }

    /*
    проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
     */
    @Test
    void taskWithGivenIdAndGeneratedIdNotConflictWithinManager() {
        Task task10 = new Task("task10", "ten task10");
        taskManager.addTask(task10);
        int IdTask10 = task10.getId();
        Task task11 = new Task("task11", "eleven task11");
        task11.setId(IdTask10);
        taskManager.addTask(task11);
        assertNotEquals(task10.getId(), task11.getId(), "присвоились одинаковые ID");
    }

    /*
    создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
     */
    @Test
    void fieldsShouldNotChangeAfterAddingToManager() {
        Task task10 = new Task("task10", "ten task10");
        task10.setStatus(StatusEnum.IN_PROGRESS);
        taskManager.addTask(task10);
        assertEquals("task10", task10.getName());
        assertEquals("ten task10", task10.getDescription());
        assertEquals(StatusEnum.IN_PROGRESS, task10.getStatus());
    }

    @Test
    void tasksAddedToHistoryManagerRetainPreviousVersionTask() {  //Сравнение на изменение в истории
        Managers.getDefault().clearAllTask();
        Task task10 = new Task("task10", "ten task10");
        task10.setId(1);
        Managers.getDefaultHistory().add(task10);
        Task task11 = new Task("task11", "eleven task11");
        task11.setId(2);
        Managers.getDefaultHistory().add(task10);
        Assertions.assertEquals("task10", Managers.getDefaultHistory().getHistory().get(1).getName());
        Assertions.assertEquals("ten task10", Managers.getDefaultHistory().getHistory().get(1).getDescription());
        Assertions.assertEquals(1, Managers.getDefaultHistory().getHistory().get(1).getId());
    }

    @Test
    void addTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void addEpic() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void addSubTask() {  //проверка что эпик нельзя добавить самого в себя.
        int id = epic1.getId();
        subtask1.setId(id);
        subtask1.setEpicId(id);
        taskManager.addSubTask(subtask1);
        Assertions.assertTrue(id != subtask1.getId(), "одинаковые ID");
    }

    @Test
    void getListTask() {
    }

    @Test
    void getListEpic() {
    }

    @Test
    void getListSubTask() {
    }

    @Test
    void clearAllTask() {
    }

    @Test
    void clearAllSubTask() {
    }

    @Test
    void clearAllEpic() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void getEpicById() {
    }

    @Test
    void getSubTaskById() {
    }

    @Test
    void removeTaskById() {
    }

    @Test
    void removeEpicById() {
    }

    @Test
    void removeSubTaskById() {
    }

    @Test
    void getEpicSubTaskById() {
    }
}