import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    private int nextId = 1;

    public void addTask(Task task) {
        task.setId(getNextId());
        taskMap.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }


    public void addEpic(Epic epic) {
        epic.setId(getNextId());
        epicMap.put(epic.getId(), epic);

    }

    public void updateEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
 /*       //Виталий, я не понимаю нужна ли здесь эта процедура обновления списка id сабтасков привязанных к эпику,
 если не нужна скажи, удалю. Мне кажется что не нужна, но на всякий случай закомментированной оставлю.
        epic.setSubtaskIds(new ArrayList<>());
        for (Subtask value : subTaskMap.values()) {
            if (value.getEpicId() == epic.getId()) {
                epic.getSubtaskIds().add(value.getEpicId());
            }
        }
  */
        UpdateEpicStatus(epic);
    }

    public void addSubTask(Subtask subTask) {
        subTask.setId(getNextId());
        subTaskMap.put(subTask.getId(), subTask);

        Epic epic = epicMap.get(subTask.getEpicId()); //достаю эпик из мапы по номеру эпикИД который узнал у субзадачи
        epic.getSubtaskIds().add(subTask.getId());  //добавляю ИД субзадачи в список который у эпика.
        UpdateEpicStatus(epic);
    }

    public void updateSubTask(Subtask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
        Epic epic = epicMap.get(subTask.getEpicId());  //достаю эпик из мапы по номеру эпикИД который узнал у субзадачи
        UpdateEpicStatus(epic);
    }

    public ArrayList<Task> getListTask() {  //список всех task
        return new ArrayList<Task>(taskMap.values());
    }

    public ArrayList<Epic> getListEpic() { //список всех epic
        return new ArrayList<Epic>(epicMap.values());
    }

    public ArrayList<Subtask> getListSubTask() { //список всех subtask
        return new ArrayList<Subtask>(subTaskMap.values());
    }

    public void clearAllTask() { //удаление всех задач
        taskMap.clear();
    }

    public void clearAllSubTask() { //удаление всех подзадач
        for (Epic value : epicMap.values()) {
            value.setSubtaskIds(new ArrayList<>());  //очищаю у каждого epic список связанных subTask
            UpdateEpicStatus(value);
        }
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

    public Subtask getSubTaskById(int id) {  //получение subTask по id
        return subTaskMap.get(id);
    }

    public void removeTaskById(int id) {  //удаление task по id
        epicMap.remove(id);
    }

    public void removeEpicById(int id) {  //удаление epic по id
        epicMap.remove(id);
        ArrayList<Integer> listIdSubTasks = new ArrayList<>(); //список для хранения id subTask связанных с удаляемым эпиком
        for (Subtask value : subTaskMap.values()) {
            if (value.getEpicId() == id) {
                listIdSubTasks.add(value.getEpicId());
            }
        }
        for (Integer listIdSubTask : listIdSubTasks) {
            subTaskMap.remove(listIdSubTask);
        }
    }

    public void removeSubTaskById(int id) {  //удаление подзадачи по id
        int epicId = subTaskMap.get(id).getEpicId();  //нахожу Id связанного с subTask эпика
        epicMap.get(epicId).getSubtaskIds().remove(id); //удаляю связь с subTask в эпиковском списке id
        subTaskMap.remove(id);
        UpdateEpicStatus(epicMap.get(epicId));
    }

    public ArrayList<Subtask> getEpicSubTaskById(int id) {  //получение списка объектов subTask по эпик id
        ArrayList<Subtask> listSubTask = new ArrayList<>();
        int epicId = epicMap.get(id).getId();
        for (Subtask value : subTaskMap.values()) {
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
            StatusEnum stat = subTaskMap.get(subtaskId).getStatus(); //это статус каждой подзадачи принадлежащая этому эпику
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

    private int getNextId(){  //возвращает текущий nextId, но после прибавляет 1.
        return nextId++;
    }
}
