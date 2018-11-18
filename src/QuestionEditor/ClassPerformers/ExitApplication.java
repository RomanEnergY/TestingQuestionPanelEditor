package QuestionEditor.ClassPerformers;

/**
 * Итерфейс действие при закрытии приложения
 *
 * Created by user on 10.10.2018.
 */
public interface ExitApplication {
    /**
     * Метод возвращающий логическое состояние при выходе из приложения
     *
     * @return возвращает значение true - если разрешен выход из программы
     */
    boolean exit();
}
