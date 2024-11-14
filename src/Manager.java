import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    private int nextId = 1;


    public Manager() {
    }


    public void addTask(Task task) {
        task.id = nextId++;
        taskMap.put(task.id, task);
    }

    public void updateTask(Task task) {
        taskMap.put(task.id, task);
    }


    public void addEpic(Epic epic) {
        epic.id = nextId++;
        epicMap.put(epic.id, epic);
    }

    public void updateEpic(Epic epic) {
        epicMap.put(epic.id, epic);
    }

    public void addSubTask(Subtask subTask) {
        subTask.id = nextId++;
        subTaskMap.put(subTask.id, subTask);

        Epic epic = epicMap.get(subTask.epicId); //достаю эпик из мапы по номеру эпикИД который узнал у субзадачи
        epic.subtaskIds.add(subTask.id); //добавляю ИД субзадачи в список который у эпика.

        int countEnumNew = 0;  //считаю кол-во статусов равных new
        int countEnumDone = 0;
        int countEnumIN_PROGRESS = 0;
        for (Integer subtaskId : epic.subtaskIds) { //из списка ID принадлежащих этому эпику беру каждый ID субтаска
            StatusEnum stat = subTaskMap.get(subtaskId).status; //это каждая подзадача(т.е. ее статус) принадлежащая этому эпику
            if (stat == StatusEnum.NEW) {
                countEnumNew++;
            } else if (stat == StatusEnum.DONE) {
                countEnumDone++;
            } else {
                countEnumIN_PROGRESS++;
            }
        }

        if (epic.subtaskIds.isEmpty() || (countEnumIN_PROGRESS == 0 && countEnumDone == 0)) {  //проверка на статус подзадач в эпике
            epic.status = StatusEnum.NEW;
        } else if (countEnumDone == epic.subtaskIds.size()) {
            epic.status = StatusEnum.DONE;
        } else {
            epic.status = StatusEnum.IN_PROGRESS;
        }
    }

    public void updateSubTask(Subtask subTask) {
        subTaskMap.put(subTask.id, subTask);
        Epic epic = epicMap.get(subTask.epicId); //достаю эпик из мапы по номеру эпикИД который узнал у субзадачи
        int countEnumNew = 0;  //считаю кол-во статусов не равных new
        int countEnumDone = 0;
        int countEnumIN_PROGRESS = 0;
        for (Integer subtaskId : epic.subtaskIds) { //из списка ID принадлежащих этому эпику беру каждый ID субтаска
            StatusEnum stat = subTaskMap.get(subtaskId).status; //это каждая подзадача(т.е. ее статус) принадлежащая этому эпику
            if (stat == StatusEnum.NEW) {
                countEnumNew++;
            } else if (stat == StatusEnum.DONE) {
                countEnumDone++;
            } else {
                countEnumIN_PROGRESS++;
            }
        }

        if (epic.subtaskIds.isEmpty() || (countEnumIN_PROGRESS == 0 && countEnumDone == 0)) {  //проверка на статус подзадач в эпике
            epic.status = StatusEnum.NEW;
        } else if (countEnumDone == epic.subtaskIds.size()) {
            epic.status = StatusEnum.DONE;
        } else {
            epic.status = StatusEnum.IN_PROGRESS;
        }
    }

    public ArrayList<Task> getListTask() {  //получаю список всех задач
        ArrayList<Task> getListTask = new ArrayList<>();
        for (Task value : taskMap.values()) {
            getListTask.add(value);
        }
        return getListTask;
    }

    public ArrayList<Epic> getListEpic() { //получаю список всех эпиков
        ArrayList<Epic> getListEpic = new ArrayList<>();
        for (Epic value : epicMap.values()) {
            getListEpic.add(value);
        }
        return getListEpic;
    }

    public ArrayList<Subtask> getListSubTask() { //получаю список всех подзадач
        ArrayList<Subtask> getListSubTask = new ArrayList<>();
        for (Subtask value : subTaskMap.values()) {
            getListSubTask.add(value);
        }
        return getListSubTask;
    }

    public void clearAllTask() { //удаление всех задач
        taskMap.clear();
    }

    public void clearAllSubTask() { //удаление всех подзадач
        subTaskMap.clear();
    }

    public void clearAllEpic() { //удаление всех подзадач
        epicMap.clear();
        subTaskMap.clear();
    }

    public Task getTaskById(int id) {  //получение задачи по id
        return taskMap.get(id);
    }

    public Epic getEpicById(int id) {  //получение эпика по id
        return epicMap.get(id);
    }

    public Subtask getSubTaskById(int id) {  //получение подзадачи по id
        return subTaskMap.get(id);
    }

    public void removeTaskById(int id) {  //удаление задачи по id
        taskMap.remove(id);
    }

    public void removeEpicById(int id) {  //удаление эпика по id
        int epicId = epicMap.get(id).id;
        epicMap.remove(id);
        ArrayList<Integer> listIdSubTasks = new ArrayList<>();
        for (Subtask value : subTaskMap.values()) {
            if (value.epicId == epicId) {
                listIdSubTasks.add(value.id);
            }
        }
        for (Integer listIdSubTask : listIdSubTasks) {
            subTaskMap.remove(listIdSubTask);
        }
    }

    public void removeSubTaskById(int id) {  //удаление подзадачи по id
        subTaskMap.remove(id);
    }

    public ArrayList<Subtask> getEpicSubTaskById(int id) {  //получение списка сабтасков по эпик id
        ArrayList<Subtask> listSubTask = new ArrayList<>();
        int epicId = epicMap.get(id).id;
        for (Subtask value : subTaskMap.values()) {
            if (value.epicId == epicId) {
                listSubTask.add(value);
            }
        }
        return listSubTask;
    }
}
