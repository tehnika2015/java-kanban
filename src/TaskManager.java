import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    private int nextId = 1;

    public void addTask(Task task) {
        task.setId(getNextId());
        setNextId(task.getId() + 1);
        getTaskMap().put(task.getId(), task);
    }

    public void updateTask(Task task) {
        getTaskMap().put(task.getId(), task);
    }


    public void addEpic(Epic epic) {
        epic.setId(getNextId());
        setNextId(epic.getId() + 1);
        getEpicMap().put(epic.getId(), epic);

    }

    public void updateEpic(Epic epic) {
//        getEpicMap().get(epic.getId()).setStatus(epic.getStatus());  // не понял может таким способом нужно?
        UpdateEpicStatus(epic);
    }

    public void addSubTask(Subtask subTask) {
        subTask.setId(getNextId());
        setNextId(subTask.getId() + 1);
        getSubTaskMap().put(subTask.getId(), subTask);

        Epic epic = getEpicMap().get(subTask.getEpicId()); //достаю эпик из мапы по номеру эпикИД который узнал у субзадачи
        epic.getSubtaskIds().add(subTask.getId());  //добавляю ИД субзадачи в список который у эпика.
        UpdateEpicStatus(epic);
    }

    public void updateSubTask(Subtask subTask) {
        getSubTaskMap().put(subTask.getId(), subTask);
        Epic epic = getEpicMap().get(subTask.getEpicId());  //достаю эпик из мапы по номеру эпикИД который узнал у субзадачи
        UpdateEpicStatus(epic);
    }

    public ArrayList<Task> getListTask() {  //список всех task
        return new ArrayList<Task>(getTaskMap().values());
    }

    public ArrayList<Epic> getListEpic() { //список всех epic
        return new ArrayList<Epic>(getEpicMap().values());
    }

    public ArrayList<Subtask> getListSubTask() { //список всех subtask
        return new ArrayList<Subtask>(getSubTaskMap().values());
    }

    public void clearAllTask() { //удаление всех задач
        getTaskMap().clear();
    }

    public void clearAllSubTask() { //удаление всех подзадач
        for (Epic value : getEpicMap().values()) {
            value.setSubtaskIds(new ArrayList<>());
            UpdateEpicStatus(value);
        }
        getSubTaskMap().clear();
    }

    public void clearAllEpic() { //удаление всех подзадач
        getEpicMap().clear();
        getSubTaskMap().clear();
    }

    public Task getTaskById(int id) {  //получение задачи по id
        return getTaskMap().get(id);
    }

    public Epic getEpicById(int id) {  //получение эпика по id
        return getEpicMap().get(id);
    }

    public Subtask getSubTaskById(int id) {  //получение subTask по id
        return getSubTaskMap().get(id);
    }

    public void removeTaskById(int id) {  //удаление task по id
        getEpicMap().remove(id);
    }

    public void removeEpicById(int id) {  //удаление epic по id
        getEpicMap().remove(id);
        ArrayList<Integer> listIdSubTasks = new ArrayList<>(); //список для хранения id субтасков связанных с удаляемым эпиком
        for (Subtask value : getSubTaskMap().values()) {
            if (value.getEpicId() == id) {
                listIdSubTasks.add(value.getEpicId());
            }
        }
        for (Integer listIdSubTask : listIdSubTasks) {
            getSubTaskMap().remove(listIdSubTask);
        }
    }

    public void removeSubTaskById(int id) {  //удаление подзадачи по id
        int epicId = getSubTaskMap().get(id).getEpicId();  //нахожу Id связанного с субтаском эпика
        getEpicMap().get(epicId).getSubtaskIds().remove(id); //удаляю связь с субтаском в эпиковском списке id
        getSubTaskMap().remove(id);
        UpdateEpicStatus(getEpicMap().get(epicId));
    }

    public ArrayList<Subtask> getEpicSubTaskById(int id) {  //получение списка сабтасков по эпик id
        ArrayList<Subtask> listSubTask = new ArrayList<>();
        int epicId = getEpicMap().get(id).getId();
        for (Subtask value : getSubTaskMap().values()) {
            if (value.getEpicId() == epicId) {
                listSubTask.add(value);
            }
        }
        return listSubTask;
    }

    private void UpdateEpicStatus(Epic epic) {
        int countEnumNEW = 0;  //считаю кол-во статусов равных new
        int countEnumDONE = 0;
        int countEnumIN_PROGRESS = 0;
        for (Integer subtaskId : epic.getSubtaskIds()) { //из списка ID принадлежащих этому эпику беру каждый ID субтаска
            StatusEnum stat = getSubTaskMap().get(subtaskId).getStatus(); //это каждая подзадача(т.е. ее статус) принадлежащая этому эпику
            if (stat == StatusEnum.NEW) {
                countEnumNEW++;
            } else if (stat == StatusEnum.DONE) {
                countEnumDONE++;
            } else {
                countEnumIN_PROGRESS++;
            }
        }

        if (epic.getSubtaskIds().isEmpty() || (countEnumIN_PROGRESS == 0 && countEnumDONE == 0)) {  //проверка на статус подзадач в эпике
            epic.setStatus(StatusEnum.NEW);
        } else if (countEnumDONE == epic.getSubtaskIds().size()) {
            epic.setStatus(StatusEnum.DONE);
        } else {
            epic.setStatus(StatusEnum.IN_PROGRESS);
        }
    }

    // геттеры и сеттеры
    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public void setTaskMap(HashMap<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public HashMap<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    public void setEpicMap(HashMap<Integer, Epic> epicMap) {
        this.epicMap = epicMap;
    }

    public HashMap<Integer, Subtask> getSubTaskMap() {
        return subTaskMap;
    }

    public void setSubTaskMap(HashMap<Integer, Subtask> subTaskMap) {
        this.subTaskMap = subTaskMap;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }
}
