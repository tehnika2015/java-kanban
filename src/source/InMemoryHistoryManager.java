package source;

import java.util.List;
import java.util.ArrayList;

//перенесите в него часть кода для работы с историей из класса InMemoryTaskManager
public class InMemoryHistoryManager implements HistoryManager {
    private static final List<Task> taskViewHistory = new ArrayList<>();

    @Override
    public void add(Task task) {  //должен помечать задачи как просмотренные
        while (Managers.getDefaultHistory().getHistory().size() >= 10) {
            Managers.getDefaultHistory().getHistory().removeFirst();
        }
        Managers.getDefaultHistory().getHistory().add(task);
    }

    @Override
    public List<Task> getHistory() {  //История просмотров задач
        return taskViewHistory;
    }
}
