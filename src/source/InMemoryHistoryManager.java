package source;

import java.util.List;
import java.util.ArrayList;

//перенесите в него часть кода для работы с историей из класса InMemoryTaskManager
public class InMemoryHistoryManager implements HistoryManager {
    private static final List<Task> taskViewHistory = new ArrayList<>();

    @Override
    public void add(Task task) {  //должен помечать задачи как просмотренные
        if (taskViewHistory.size() >= 10) {
            taskViewHistory.removeFirst();
            taskViewHistory.add(task);
        } else {
            taskViewHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {  //История просмотров задач
        return taskViewHistory;
    }
}
