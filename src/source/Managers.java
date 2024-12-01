package source;

public class Managers {

    public static TaskManager getDefault() {  //типом его возвращаемого значения будет TaskManager.
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){ //должен возвращать объект InMemoryHistoryManager — историю просмотров.
        return new InMemoryHistoryManager();
    }
}
