package source;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void updateTask(Task task);

    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    void addSubTask(Subtask subTask);

    void updateSubTask(Subtask subTask);

    ArrayList<Task> getListTask();

    ArrayList<Epic> getListEpic();

    ArrayList<Subtask> getListSubTask();

    void clearAllTask();

    void clearAllSubTask();

    void clearAllEpic();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubTaskById(int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    ArrayList<Subtask> getEpicSubTaskById(int id);

    List<Task> getHistory();
}
