package QuestionEditor.ClassPerformers;

/** Класс хранит в себе данные по ответу, возможность редактирования динамически не реализованна
 * Created by user on 24.12.2017.
 */
public class AnswerData {
    private String answer; // Текст ответа
    private boolean isRight; // Верно или ложь

    AnswerData(String answer, boolean isRight) {
        this.answer = answer;
        this.isRight = isRight;
    }

    /**
     * Метод возвращает текст ответа
     * @return текст ответа
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Метод возвращает логическое состояние на текст ответа
     * @return логическое состояние - true - верный ответ
     */
    public boolean isRight() {
        return isRight;
    }

    @Override
    public String toString() {
        return "AnswerData{" +
                "isRight=" + isRight +
                ", answer='" + answer + '\'' +
                '}';
    }
}
