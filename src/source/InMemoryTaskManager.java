package source;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    private int nextId = 1;


    @Override
    public void addTask(Task task) {
        task.setId(getNextId());
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }


    @Override
    public void addEpic(Epic epic) {
        epic.setId(getNextId());
        epicMap.put(epic.getId(), epic);

    }

    @Override
    public void updateEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
        UpdateEpicStatus(epic);
    }

    @Override
    public void addSubTask(Subtask subTask) {
        subTask.setId(getNextId());
        subTaskMap.put(subTask.getId(), subTask);

        Epic epic = epicMap.get(subTask.getEpicId()); //достаю эпик из мапы по номеру эпикИД который узнал у субзадачи
        epic.getSubtaskIds().add(subTask.getId());  //добавляю ИД субзадачи в список который у эпика.
        UpdateEpicStatus(epic);
    }

    @Override
    public void updateSubTask(Subtask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
        Epic epic = epicMap.get(subTask.getEpicId());  //достаю эпик из мапы по номеру эпикИД который узнал у субзадачи
        UpdateEpicStatus(epic);
    }

    @Override
    public ArrayList<Task> getListTask() {  //список всех task
        return new ArrayList<Task>(taskMap.values());
    }

    @Override
    public ArrayList<Epic> getListEpic() { //список всех epic
        return new ArrayList<Epic>(epicMap.values());
    }

    @Override
    public ArrayList<Subtask> getListSubTask() { //список всех subtask
        return new ArrayList<Subtask>(subTaskMap.values());
    }

    @Override
    public void clearAllTask() { //удаление всех задач
        taskMap.clear();
    }

    @Override
    public void clearAllSubTask() { //удаление всех подзадач
        for (Epic value : epicMap.values()) {
            value.setSubtaskIds(new ArrayList<>());  //очищаю у каждого epic список связанных subTask
            UpdateEpicStatus(value);
        }
        subTaskMap.clear();
    }

    @Override
    public void clearAllEpic() { //удаление всех подзадач
        epicMap.clear();
        subTaskMap.clear();
    }

    @Override
    public Task getTaskById(int id) {  //получение задачи по id
        Managers.getDefaultHistory().add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {  //получение эпика по id
        Managers.getDefaultHistory().add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {  //получение subTask по id
        Managers.getDefaultHistory().add(subTaskMap.get(id));
        return subTaskMap.get(id);
    }

    @Override
    public void removeTaskById(int id) {  //удаление task по id
        epicMap.remove(id);
    }

    @Override
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

    @Override
    public void removeSubTaskById(int id) {  //удаление подзадачи по id
        int epicId = subTaskMap.get(id).getEpicId();  //нахожу Id связанного с subTask эпика
        epicMap.get(epicId).getSubtaskIds().remove(id); //удаляю связь с subTask в эпиковском списке id
        subTaskMap.remove(id);
        UpdateEpicStatus(epicMap.get(epicId));
    }

    @Override
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
            StatusEnum statusSubTask = subTaskMap.get(subtaskId).getStatus(); //это статус каждой подзадачи принадлежащая этому эпику
            switch (statusSubTask) {
                case StatusEnum.NEW:
                    countEnumNEW++;
                    break;
                case StatusEnum.DONE:
                    countEnumDONE++;
                    break;
                default:
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

    private int getNextId() {  //возвращает текущий nextId, но после прибавляет 1.
        return nextId++;
    }
}
