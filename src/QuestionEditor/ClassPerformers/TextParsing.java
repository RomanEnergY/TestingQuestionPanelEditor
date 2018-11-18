package QuestionEditor.ClassPerformers;

import java.util.ArrayList;

/**
 * Created by user on 29.12.2017.
 */
public class TextParsing {
    private int anInt; // Содержит в себе номер строки StrFormat который первый совпал по формату поиска в строке, в противном случае -1 говорит, что текст не распознан
    private String textDefined;
    private String textNotDefined;

    // Формат определения начала чтения блока вопрос - вопрос
    final static String[][] FORMAT_DATE_PROTOCOL = new String[][]{
            {"<td", ">Дата</td><td", ">", "</td>"},
    };
    // Формат определения начала чтения блока вопрос - вопрос
    final static String[][] FORMAT_PROGRAM_PROTOCOL = new String[][]{
            {"<td", ">Программа</td><td", ">", "</td>"},
    };
    // Формат определения начала чтения блока вопрос - вопрос
    final static String[][] FORMAT_SECTION_PROTOCOL = new String[][]{
            {"<td", "<u>ТЕМА: ", "</u>", "</td>"},
    };
    // Формат определения окончания чтения блока вопрос - вопрос
    final static String[][] FORMAT_END_QUESTION_LINE = new String[][]{
            {"Председатель комиссии", "Члены комиссии:-", "</td>"},
    };
    // Формат определения чтения в блоке номера вопроса
    final static String[][] FORMAT_CODE_QUESTION = new String[][]{
            {"<td ", "ВОПРОС № ", " - ", "</td>"},
    };
    // Формат определения чтения в блоке наименования вопроса и справочной литературы
    final static String[][] FORMAT_TEXT_QUESTION_AND_REFERENCE_BOOKS = new String[][]{
            {"Вопрос:<", "<td ", ">", "</td>"},
    };
    // Формат определения в строке вопроса
    final static String[][] FORMAT_TEXT_FINISH_QUESTION = new String[][]{
            {"Вопрос:</td><td ", ">"},
    };

    final static String[][] FORMAT_TEXT_ANSWER = new String[][]{
            {"Все ответы:<", "<td ", "</td>"}
    };

    final static String[][] FORMAT_TEXT_FINISH_ANSWER = new String[][]{
            {"Все ответы:<", "<td ", ">"}
    };

    final static String NOT_DEFINED = "NOT_DEFINED";

    int getAnInt() {
        return anInt;
    }
    String getTextDefined() {
        return textDefined;
    }
    String getTextNotDefined() {
        return textNotDefined;
    }

    /**
     * Статический метод проверяет в передаваемой строке String text наличие указанного String[][] strFormat,
     * при определении возвращает true, в противном случае false - указанный String[][] strFormat отсутствует
     *
     * @param text строка поиска
     * @param strFormat формат поиска
     * @return возвращает true, если указанный String[][] strFormat присудствует в строке поиска
     */
    static boolean isTextParsing(String text, String[][] strFormat){
        boolean queue;
        int index;
        for (int i = 0; i < strFormat.length; i++) {
            index = -1;
            queue = true;
            for (int j = 0; j < strFormat[i].length && queue; j++) {
                if (text.indexOf(strFormat[i][j], index) >= index) {
                    // index очередности позиций в строке поиска
                    index = text.indexOf(strFormat[i][j], index) + strFormat[i][j].length();
                    if (j == strFormat[i].length - 1)
                        return true;
                } else
                    queue = false;
            }
        }

        return false;
    }
    TextParsing(String text, String[][] strFormat) {
        anInt = -1;

        boolean stopParsingOfFormat; //
        int index;

        for (int i = 0; i < strFormat.length; i++) {
            index = -1;
            stopParsingOfFormat = true;
            for (int j = 0; j < strFormat[i].length && stopParsingOfFormat; j++) {
                if (text.indexOf(strFormat[i][j], index) >= index) {
                    index = text.indexOf(strFormat[i][j], index) + strFormat[i][j].length();
                } else
                    stopParsingOfFormat = false;
            }

            if (stopParsingOfFormat) {
                anInt = i;
                break;
            }


        }

        if (anInt != -1) {
            stopParsingOfFormat = true;
            int startRead = text.indexOf(strFormat[anInt][0]);
            int finishRead = -1;

            for (int j = 0; j < strFormat[anInt].length && stopParsingOfFormat; j++) {
                if (text.indexOf(strFormat[anInt][j], finishRead) > finishRead) {
                    finishRead = text.indexOf(strFormat[anInt][j], finishRead)  + strFormat[anInt][j].length();
                    if (j == strFormat[anInt].length - 1){
                        this.textDefined = parsingText(text, startRead, finishRead); // <td colspan="9" class="s2">
                        this.textNotDefined = parsingText(text, 0, startRead) + parsingText(text, finishRead, text.length()); // Вопрос:</td>Разрешается ли работа с электроинструментом на приставных лестницах, стремянках?


                    }
                } else
                    stopParsingOfFormat = false;
            }
        }
        else {
            this.textDefined = "NOT_DEFINED";
            this.textNotDefined = text;
        }
    }
    TextParsing(String text, String[][] strFormat, int index) {
        ArrayList<String> strings = new ArrayList<>();
        anInt = -1;

        boolean stopParsingOfFormat; //
        int indexStart = 0;
        int indexFinish = 0;

        for (int i = 0; i < strFormat.length; i++) {
            indexStart = 0;
            stopParsingOfFormat = true;
            for (int j = 0; j < strFormat[i].length && stopParsingOfFormat; j++) {
                if (text.indexOf(strFormat[i][j], indexStart) >= indexStart) {
                    indexFinish = text.indexOf(strFormat[i][j], indexStart) + strFormat[i][j].length();
                    if (j == 0){
                        strings.add(parsingText(text, 0, indexFinish - strFormat[i][j].length()));
                    }
                    else {
                        strings.add(parsingText(text, indexStart, indexFinish - strFormat[i][j].length()));
                    }
                    indexStart = indexFinish;
                } else {
                    strings.add(parsingText(text, indexStart, text.length()));
                    stopParsingOfFormat = false;
                }
            }

            if (stopParsingOfFormat) {
                anInt = i;
                break;
            }
        }
        strings.add(parsingText(text, indexStart, text.length()));


        if (anInt != -1) {
            this.textDefined = strings.get(index);

            stopParsingOfFormat = true;
            int startRead = text.indexOf(strFormat[anInt][0]);
            int finishRead = -1;

            for (int j = 0; j < strFormat[anInt].length && stopParsingOfFormat; j++) {
                if (text.indexOf(strFormat[anInt][j], finishRead) > finishRead) {
                    finishRead = text.indexOf(strFormat[anInt][j], finishRead) + strFormat[anInt][j].length();
                    if (j == strFormat[anInt].length - 1) {
                        this.textNotDefined = parsingTextCut(text, this.textDefined);
                    }
                } else
                    stopParsingOfFormat = false;
            }
        }
        else {
            this.textDefined = "NOT_DEFINED";
            this.textNotDefined = text;
        }
        strings.size();
    }
    private String parsingTextCut(String text, String textCut) {
        return parsingText(text, 0, text.indexOf(textCut)) + parsingText(text, text.indexOf(textCut) + textCut.length(), text.length());
    }
    private String parsingText(String text, int startRead, int finishRead) {
        String strReturn = "";
        StringBuilder sb = new StringBuilder();

        for (int i = startRead; i < finishRead; i++) {
            strReturn = sb.append(text.charAt(i)).toString();
        }

        return strReturn;
    }
    @Override
    public String toString() {
        return "StrFormat{" +
                "\nanInt=" + anInt +
                "\ntextDefined='" + textDefined + '\'' +
                "\ntextNotDefined='" + textNotDefined + '\'' +
                "\n}";
    }
}
