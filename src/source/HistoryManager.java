package source;

import java.util.List;
// отдельный интерфейс для управления историей просмотров
public interface HistoryManager {

    void add(Task task);  //должен помечать задачи как просмотренные
    List<Task> getHistory();  //возвращать их список
}
