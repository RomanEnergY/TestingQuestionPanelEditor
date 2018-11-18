package QuestionEditor.ClassPerformers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by user on 23.12.2017.
 */
public class QuestionData {
    private static String dateStatic;
    private static String programStatic;
    private static String sectionStatic;

    private String date; // Дата формирования вопроса
    private String program; // Программа тренировки
    private String section; // Раздел
    private int codeQuestion; // Код вопроса
    private String textOfQuestion; // Тест вопроса
    private String referenceBooks; // Справочная литература
    private String book; // Краткая справочная литература
    private ArrayList<AnswerData> answers; // Список ответов
    private String notDefined = ""; // Нераспознанные данные

    public QuestionData(String date, String program, String section, int codeQuestion, String textOfQuestion, String strReferenceBooks, ArrayList<AnswerData> answers) {
        this.date = date;
        this.program = program;
        this.section = section;
        this.codeQuestion = codeQuestion;
        this.textOfQuestion = textOfQuestion;
        this.referenceBooks = strReferenceBooks;
        this.book = createBook(this.referenceBooks);
        this.answers = answers;
    }

    public QuestionData() {
        this.date = null;
        this.program = null;
        this.section = null;
        this.codeQuestion = 0;
        this.textOfQuestion = null;
        this.referenceBooks = null;
        this.book = null;
        this.answers = null;
    }

    // Сетеры объекта
    static void setProgramStatic(String programStatic) {
        QuestionData.programStatic = programStatic;
    }

    static void setSectionStatic(String sectionStatic) {
        QuestionData.sectionStatic = sectionStatic;
    }

    static void setDateStatic(String dateStatic) {
        QuestionData.dateStatic = dateStatic;
    }

    // Геттеры объекта
    static String getDateStatic() {
        return dateStatic;
    }

    static String getProgramStatic() {
        return programStatic;
    }

    static String getSectionStatic() {
        return sectionStatic;
    }

    public String getDate() {
        return date;
    }

    public String getProgram() {
        return program;
    }

    public String getSection() {
        return section;
    }

    public int getCodeQuestion() {
        return codeQuestion;
    }

    public String getTextOfQuestion() {
        return textOfQuestion;
    }

    public String getReferenceBooks() {
        return referenceBooks;
    }

    public String getBook() {
        return book;
    }

    public ArrayList<AnswerData> getAnswers() {
        return answers;
    }

    public ArrayList<AnswerData> getShuffleAnswers() {
        ArrayList<AnswerData> arrayList = (ArrayList<AnswerData>) answers.clone();
        Collections.shuffle(arrayList);
        return arrayList;
    }

    public int getCountAnswerIsRight() {
        int countAnswerIsRight = 0;
        for (AnswerData answerData : getAnswers()) {
            if (answerData.isRight()) {
                countAnswerIsRight++;
            }
        }
        return countAnswerIsRight;
    }

    public int getCountAnswer() {
        return this.getAnswers().size();
    }

    public String getNotDefined() {
        return notDefined;
    }

    // переопределенные методы
    @Override
    public String toString() {
        return "QuestionData{" +
                "\n\tdate='" + date + '\'' +
                "\n\tprogram='" + program + '\'' +
                "\n\tsection='" + section + '\'' +
                "\n\tcodeQuestion=" + codeQuestion +
                "\n\ttextOfQuestion='" + textOfQuestion + '\'' +
                "\n\tstrReferenceBooks='" + referenceBooks + '\'' +
                "\n\tanswers=" + answers +
                "\n\tnotDefined='" + notDefined + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionData that = (QuestionData) o;

        if (codeQuestion != that.codeQuestion) return false;
        if (program != null ? !program.equals(that.program) : that.program != null) return false;
        if (section != null ? !section.equals(that.section) : that.section != null) return false;
        if (textOfQuestion != null ? !textOfQuestion.equals(that.textOfQuestion) : that.textOfQuestion != null)
            return false;
        if (referenceBooks != null ? !referenceBooks.equals(that.referenceBooks) : that.referenceBooks != null)
            return false;
        if (book != null ? !book.equals(that.book) : that.book != null) return false;
        return answers != null ? answers.equals(that.answers) : that.answers == null;
    }

    @Override
    public int hashCode() {
        int result = program != null ? program.hashCode() : 0;
        result = 31 * result + (section != null ? section.hashCode() : 0);
        result = 31 * result + codeQuestion;
        result = 31 * result + (textOfQuestion != null ? textOfQuestion.hashCode() : 0);
        result = 31 * result + (referenceBooks != null ? referenceBooks.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        result = 31 * result + (answers != null ? answers.hashCode() : 0);
        return result;
    }

    /**
     * @param s
     */
    public QuestionData(String s) {
        notDefined = s;

        // Блок определения codeQuestion - код вопроса
        TextParsing textParsingCodeQuestion = new TextParsing(notDefined, TextParsing.FORMAT_CODE_QUESTION, 3);
        codeQuestion = definedCodeQuestion(textParsingCodeQuestion.getTextDefined()); // определяем поле codeQuestion
        notDefined = textParsingCodeQuestion.getTextNotDefined(); // нераспознанный текст

        // Блок определения questionAndReferenceBooks - тест вопроса и справочная литература
        TextParsing textParsingTextOfQuestionAndReferenceBooks = new TextParsing(notDefined, TextParsing.FORMAT_TEXT_QUESTION_AND_REFERENCE_BOOKS);
        String[] strings = definedTextOfQuestionAndReferenceBooks(textParsingTextOfQuestionAndReferenceBooks.getTextDefined()); // [0] - textOfQuestion, [1] - referenceBooks
        notDefined = textParsingTextOfQuestionAndReferenceBooks.getTextNotDefined(); // нераспознанный текст

        // определяем текст вопроса и справочную литературу по принципу:
        // определяем текст FORMAT_TEXT_QUESTION - это html метки, они нам не нужны, а нераспознанный текст - это является текстом вопроса
        TextParsing textParsing_TEXT_FINISH_QUESTION = new TextParsing(strings[0], TextParsing.FORMAT_TEXT_FINISH_QUESTION);
        textOfQuestion = textParsing_TEXT_FINISH_QUESTION.getTextNotDefined(); // Тест вопроса
        // Приведение текста, удаление ненужной информации
        textOfQuestion = bringingText(textOfQuestion);
        referenceBooks = bringingText(strings[1]);

        TextParsing textParsing_TEXT_ANSWER = new TextParsing(notDefined, TextParsing.FORMAT_TEXT_ANSWER);
        TextParsing textParsing_TEXT_FINISH_ANSWER = new TextParsing(textParsing_TEXT_ANSWER.getTextDefined(), TextParsing.FORMAT_TEXT_FINISH_ANSWER);
        answers = definedAnswers(textParsing_TEXT_FINISH_ANSWER.getTextNotDefined());
        notDefined = textParsing_TEXT_ANSWER.getTextNotDefined();

        this.book = createBook(this.referenceBooks);

        this.date = dateStatic;
        this.program = programStatic;
        this.section = sectionStatic;
    }

    private String createBook(String referenceBooks) {
        // Блок определения литературы
        String s = "DEFINED";
        if (referenceBooks.contains("Правила по охране труда при эксплуатации электроустановок")) {
            s = "Правила по охране труда при эксплуатации электроустановок (утв. Приказом Минтруда от 24.07.2013 № 328н)";
        } else if (referenceBooks.contains("СО 153-34.03.603-2003")) {
            s = "СО 153-34.03.603-2003 Инструкция по применению и испытанию средств защиты, используемых в электроустановках";
        } else if (referenceBooks.contains("ПБРИП")) {
            s = "ПБРИП";
        } else if (referenceBooks.contains("ИОПП")) {
            s = "ИОПП";
        } else if (referenceBooks.contains("СТО 34.01-27.1-001-2014")) {
            s = "СТО 34.01-27.1-001-2014";
        } else if (referenceBooks.contains("Федеральный закон № 123-ФЗ")) {
            s = "СТО 34.01-27.1-001-2014";
        } else if (referenceBooks.contains("ПТЭ-2003")) {
            s = "ПТЭ-2003";
        } else if (referenceBooks.contains("приказ Минэнерго № 289 от 30.06.03")) {
            s = "Инструкция по предотвращению и ликвидации аварий в электрической части энергосистем, приказ Минэнерго № 289 от 30.06.03 г.";
        } else if (referenceBooks.contains("ПУЭ")) {
            s = "ПУЭ";
        } else if (referenceBooks.contains("ПРП-2000")) {
            s = "ПРП-2000";
        } else if (referenceBooks.contains("приказ МЭ РФ № 266 от 30.06.03")) {
            s = "Инструкция по переключениям в электроустановках, приказ МЭ РФ № 266 от 30.06.03 г.";
        } else if (referenceBooks.contains("ОиНИЭО")) {
            s = "ОиНИЭО";
        } else if (referenceBooks.contains("РД 34.45-51.300-97")) {
            s = "РД 34.45-51.300-97";
        }


        return s;
    }

    private ArrayList<AnswerData> definedAnswers(String text) {
//        System.out.println(this.codeQuestion + " '" + text + '\'');
        // Отлечительной особенностью передаваемой строки является то, что между первым, первый++ и последним ответом есть метка переноса строки "<br>" и следующи знак является цифрой
        // так же за цифрой может стоять тоже цифра, но по окончанию метки "<br>" + "цифра" идет знак '.' - окончательный
        // в вопросах присутствует метка <b>(<i>верно</i>)</b> означающая правильный ответ.

        // Определяем кол-во предлагаемых ответов
        ArrayList<AnswerData> answerData = new ArrayList<>();

        String format = "<br>";
        String s;

        int count = 0;
        int startRead = 2;
        int index = 0;

        do {
            // определяем место положения метки format
            if (text.indexOf(format, count) > count) {
                count = text.indexOf(format, count) + format.length() - 1;
                index = 1;

                do {
                    if (Character.isDigit(text.charAt(count + index))) {
                        index++;
                    } else if (text.charAt(count + index) == '.') {
                        s = bringingText(parsingText(text, startRead, count - index - 1));
                        answerData.add(new AnswerData(definedAnswerDataToAnswer(s), definedAnswerDataToIsRight(s)));
                        count = count + index + 1;
                        startRead = count;
                        break;
                    } else
                        break;

                } while (true);
            } else {
                s = bringingText(parsingText(text, startRead, text.length()));
                answerData.add(new AnswerData(definedAnswerDataToAnswer(s), definedAnswerDataToIsRight(s)));
                break;
            }
        }
        while (true);

        return answerData;
    }

    private boolean definedAnswerDataToIsRight(String text) {
        String format = "<b>(<i>верно</i>)</b>";
        if (text.contains(format))
            return true;
        else
            return false;
    }

    private String definedAnswerDataToAnswer(String text) {
        String format = "<b>(<i>верно</i>)</b>";
        if (text.contains(format)) {
            text = bringingText(parsingText(text, text.indexOf(format) + format.length(), text.length()));
        }

        return text;
    }

    // Приведение текста, удаление ненужной информации
    static private String bringingText(String text) {
        text = text.replaceAll("</td>", ""); // заменяем все значение "  " - три пробела на " " - один пробел
        text = text.replaceAll("   ", ""); // заменяем все значение "  " - три пробела на " " - один пробел
        text = text.replaceAll("  ", ""); // заменяем все значение "  " - два пробела на " " - один пробел
        text = text.replaceAll("<br>", ""); // заменяем все значение "<br>" на ""
        text = text.replaceAll("&quot;", "\""); // заменяем все значение "&quot;" на "\""
//        text = text.replaceAll("<", ""); // заменяем все значение "&quot;" на "\""
        text = text.trim(); // удаляем пробелы в тексте в начале и в конце строки

        return text;
    }

    private String[] definedTextOfQuestionAndReferenceBooks(String text) {
        // Отлечительная особенность этой строки в том, что в ее конце между знаками '(' ')' находится справочная литература
        // Есть еще отличия, справочная литература имеет вид данных "(Книга(коментарий) п.3.4.54)"
        // Определяем место нахождения данных меток, записываем между метка данные в strings[1], остальное в strings[0]
        // strings[0] - текст от начала до метки
        // strings[1] - от метки до метки
        String[] strings = new String[]{TextParsing.NOT_DEFINED, TextParsing.NOT_DEFINED};
        boolean b = true;
        int index = 0;
        for (int i = text.length() - 1; i >= 0 && b; i--) {
            if (text.charAt(i) == ')') {
                for (int j = i; j >= 0; j--) {
                    if (text.charAt(j) == '?') {
                        strings[0] = parsingText(text, 0, j);
                        strings[1] = parsingText(text, j + 1, i);

                        b = false;
                        break;
                    } else if (text.charAt(j) == ')') {
                        index++;
                    } else if (text.charAt(j) == '(') {
                        if (index > 1) {
                            index--;
                        } else {
                            strings[0] = parsingText(text, 0, j);
                            strings[1] = parsingText(text, j + 1, i);

                            b = false;
                            break;
                        }
                    }
                }
            }
        }

        return strings;
    }

    /**
     * Определяем из строки в которой находится код вопроса, непосредственно код вопроса и преобразуем его в объект Integer
     * Отлечительная особенность, код вопроса находится в конце строки.
     * Принцип определения кода вопроса: В цикле определяем последний символ и если он является 'числом', входим в следующий цикл,
     * который ожидает появление символа 'не число' и считываем данные от 'не число'+1 до определения числа в первом цикле.
     *
     * @param text
     * @return
     */
    private int definedCodeQuestion(String text) {
        try {
            return Integer.valueOf(text); // Преобразуем строковое значение в объект Integer

        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Метод реализует чтение данных из файла и возвращает лист данных QuestionData
     * 1. считывает файл.html по строчно от метки "<tr " до метки "</tr>" и записывает данные в лист strings
     * 2. данные strings проверяются на наличие в них данных по static String dateStatic, private static String programStatic и private static String sectionStatic;
     * 3. данные strings проверяются на наличие в них метки TextParsing.FORMAT_CODE_QUESTION, если есть то определяем следующую метку TextParsing.FORMAT_CODE_QUESTION
     * или TextParsing.FORMAT_END_QUESTION_LINE и посредством склеивания данных strings в одну строку передаем в конструктор QuestionData(String s)
     * 4. полученный объект записываем в лист questionDataArrayList
     *
     * @param parsText путь и наименование файла данных для считывания
     * @return questionDataArrayList
     */
    static Collection<? extends QuestionData> getCollectionQuestionDate(String parsText) {
        ArrayList<QuestionData> questionDataArrayList = new ArrayList<>();
        QuestionData questionData;

        ArrayList<String> strings = new ArrayList<>();
        try {
            File file = new File(parsText);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            String s = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains("<tr ")) {
                    s = line;
                    if (line.contains("</tr>")) {
                        strings.add(replaceAll(s));
                    }
                } else if (line.contains("</tr>")) {
                    s = s + line;
                    strings.add(replaceAll(s));
                } else if (!s.equals("")) {
                    s = s + line;
                }
            }
        } catch (Exception ex) {
            getExceptionMessage(ex);
        }


        TextParsing textParsing;
        String string = "";
        for (int i = 0; i < strings.size(); i++) {
            // Определяем строку, если в ней имеется дата формирования протокола
            if (TextParsing.isTextParsing(strings.get(i), TextParsing.FORMAT_DATE_PROTOCOL)) {
                textParsing = new TextParsing(strings.get(i), TextParsing.FORMAT_DATE_PROTOCOL, 3);
                QuestionData.setDateStatic(textParsing.getTextDefined());
            }
            // Определяем строку, если в ней имеется программа тренировки
            if (TextParsing.isTextParsing(strings.get(i), TextParsing.FORMAT_PROGRAM_PROTOCOL)) {
                textParsing = new TextParsing(strings.get(i), TextParsing.FORMAT_PROGRAM_PROTOCOL, 3);
                QuestionData.setProgramStatic(textParsing.getTextDefined());
            }
            // Определяем строку, если в ней имеется тема или раздел тренеровки
            if (TextParsing.isTextParsing(strings.get(i), TextParsing.FORMAT_SECTION_PROTOCOL)) {
                textParsing = new TextParsing(strings.get(i), TextParsing.FORMAT_SECTION_PROTOCOL, 2);
                QuestionData.setSectionStatic(textParsing.getTextDefined());
            }
            // Определяем строку, если в ней имеется вопрос
            if (TextParsing.isTextParsing(strings.get(i), TextParsing.FORMAT_CODE_QUESTION)) {
                for (int j = 0; j < strings.size(); j++) {

                    if (TextParsing.isTextParsing(strings.get(j + i), TextParsing.FORMAT_CODE_QUESTION) && j != 0) {
                        questionData = new QuestionData(string);
                        questionDataArrayList.add(questionData);
                        string = "";
                        break;
                    } else if (TextParsing.isTextParsing(strings.get(j + i), TextParsing.FORMAT_END_QUESTION_LINE)) {
                        questionData = new QuestionData(string);
                        questionData.isCorrectQuestionData();
                        questionDataArrayList.add(questionData);
                        string = "";
                        break;
                    } else {
                        string = string + strings.get(j + i);
                    }
                }

            }
            // Если нет вышеизложенного, переходим к разбору следующей строки

        }

        return questionDataArrayList;
    }

    static HashMap<QuestionData, ResponseLabel> getParsToHashMap(String parsText) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        QuestionData questionData;
        ResponseLabel responseLabel = null;

        int sourceOffset = 0;
        int sourceCount = sourceOffset;

        while (parsText.indexOf(ParsingConstant.FORMAT_QUESTION_DATA[0], sourceCount) != -1) {
            sourceOffset = parsText.indexOf(ParsingConstant.FORMAT_QUESTION_DATA[0], sourceCount);
            sourceCount = parsText.indexOf(ParsingConstant.FORMAT_QUESTION_DATA[1], sourceOffset);
            stringArrayList.add(parsText.substring(sourceOffset, sourceCount + ParsingConstant.FORMAT_QUESTION_DATA[1].length()));
//            sourceCount++;
        }

        HashMap<QuestionData, ResponseLabel> questionDataResponseLabelHashMap = new HashMap<>(stringArrayList.size());
        for (String s : stringArrayList) {
            questionData = new QuestionData();
            questionData.date = questionData.parsingText(s, ParsingConstant.FORMAT_DATE);
            questionData.program = questionData.parsingText(s, ParsingConstant.FORMAT_PROGRAM);
            questionData.section = questionData.parsingText(s, ParsingConstant.FORMAT_SECTION);
            questionData.codeQuestion = Integer.parseInt(questionData.parsingText(s, ParsingConstant.FORMAT_CODE_QUESTION));
            questionData.textOfQuestion = questionData.parsingText(s, ParsingConstant.FORMAT_TEXT_OF_QUESTION);
            questionData.book = questionData.parsingText(s, ParsingConstant.FORMAT_BOOKS);
            questionData.referenceBooks = questionData.parsingText(s, ParsingConstant.FORMAT_REFERENCE_BOOKS);
            questionData.answers = questionData.parsingAnswers(s, ParsingConstant.FORMAT_BLOCK_ANSWERS);
            switch (questionData.parsingText(s, ParsingConstant.FORMAT_RESPONSE_LABEL)) {
                case "NO_ANSWER":
                    responseLabel = ResponseLabel.NO_ANSWER;
                    break;
                case "CORRECT_ANSWER":
                    responseLabel = ResponseLabel.CORRECT_ANSWER;
                    break;
                case "WRONG_ANSWER":
                    responseLabel = ResponseLabel.WRONG_ANSWER;
                    break;
            }

            //TODO реализовать чтение вопроса и ответа далее реализовать флаг ответа
            questionDataResponseLabelHashMap.put(questionData, responseLabel);
        }

        return questionDataResponseLabelHashMap;
    }

    private ArrayList<AnswerData> parsingAnswers(String parsText, String[] formatBlock) {
        ArrayList<AnswerData> answerData = new ArrayList<>();
        String textAnswer = "";
        boolean isRight = false;
        int sourceOffset = parsText.indexOf(formatBlock[0]);
        int sourceCount = parsText.indexOf(formatBlock[1], sourceOffset);
        parsText = parsText.substring(sourceOffset, sourceCount + formatBlock[1].length());

        sourceCount = 0;
        while (parsText.indexOf(ParsingConstant.FORMAT_ANSWERS[0], sourceCount) != -1) {
            sourceOffset = parsText.indexOf(ParsingConstant.FORMAT_ANSWERS[0], sourceCount);
            sourceCount = parsText.indexOf(ParsingConstant.FORMAT_ANSWERS[1], sourceOffset);
            textAnswer = this.parsingText(parsText, ParsingConstant.FORMAT_ANSWERS, sourceOffset);

            sourceOffset = parsText.indexOf(ParsingConstant.FORMAT_IS_RIGHT[0], sourceCount);
            sourceCount = parsText.indexOf(ParsingConstant.FORMAT_IS_RIGHT[1], sourceOffset);
            if (this.parsingText(parsText, ParsingConstant.FORMAT_IS_RIGHT, sourceOffset).equals("true")) {
                isRight = true;
            } else {
                isRight = false;
            }

            answerData.add(new AnswerData(textAnswer, isRight));
            sourceCount++;
        }

        return answerData;
    }

    private String parsingText(String parsText, String[] formatBlock) {
        return parsingText(parsText, formatBlock, 0);
    }

    private String parsingText(String parsText, String[] formatBlock, int sourceOffset) {
        int sourceCount;

        sourceOffset = parsText.indexOf(formatBlock[0], sourceOffset) + formatBlock[0].length();
        sourceCount = parsText.indexOf(formatBlock[1], sourceOffset);

        return parsText.substring(sourceOffset, sourceCount);
    }

    /**
     * Метод проверяет и возвращает значение true, если все поля объекта QuestionData содержат корректные данные
     *
     * @return true - все поля объекта QuestionData содержат корректные данные
     * false - одно из полей объекта QuestionData содержат некорректные данные
     */
    public boolean isCorrectQuestionData() {
        if (!this.isDate())
            return false;
        else if (!this.isProgram())
            return false;
        else if (!this.isSection())
            return false;
        else if (!this.isCodeQuestion())
            return false;
        else if (!this.isTextOfQuestion())
            return false;
        else if (!this.isReferenceBooks())
            return false;
        else if (!this.isAnswers())
            return false;
        else
            return true;
    }

    /**
     * Метод проверяет и возвращает значение true, если поле date объекта QuestionData содержат корректные данные
     *
     * @return true - параметр поле содержат корректные данные и не равно пустате
     * false - параметр поле содержат не корректные данные или равно пустате
     */
    boolean isDate() {
        return isText(this.getDate());
    }

    /**
     * Метод проверяет и возвращает значение true, если поле program объекта QuestionData содержат корректные данные
     *
     * @return true - параметр поле содержат корректные данные и не равно пустате
     * false - параметр поле содержат не корректные данные или равно пустате
     */
    boolean isProgram() {
        return isText(this.getProgram());
    }

    /**
     * Метод проверяет и возвращает значение true, если поле section объекта QuestionData содержат корректные данные
     *
     * @return true - параметр поле содержат корректные данные и не равно пустате
     * false - параметр поле содержат не корректные данные или равно пустате
     */
    boolean isSection() {
        return isText(this.getSection());
    }

    /**
     * Метод проверяет и возвращает значение true, если поле codeQuestion объекта QuestionData содержат корректные данные
     *
     * @return true - параметр поле содержат корректные данные и не равно пустате
     * false - параметр поле содержат не корректные данные или равно пустате
     */
    boolean isCodeQuestion() {
        if (this.getCodeQuestion() == -1)
            return false;
        else
            return true;
    }

    /**
     * Метод проверяет параметр поле textOfQuestion объекта QuestionData
     *
     * @return true - параметр String text содержат корректные данные и не равно пустате
     * false - параметр String text содержат не корректные данные или равно пустате
     */
    boolean isTextOfQuestion() {
        return isText(this.getTextOfQuestion());
    }

    /**
     * Метод проверяет параметр поле strReferenceBooks объекта QuestionData
     *
     * @return true - параметр String text содержат корректные данные и не равно пустате
     * false - параметр String text содержат не корректные данные или равно пустате
     */
    boolean isReferenceBooks() {
        return isText(this.getReferenceBooks());
    }

    /**
     * Метод проверяет параметр поле strReferenceBooks объекта QuestionData
     *
     * @return true - параметр String text содержат корректные данные и не равно пустате
     * false - параметр String text содержат не корректные данные или равно пустате
     */
    boolean isAnswers() {
        if (this.getAnswers().size() == 0)
            return false;
        for (AnswerData answerData : getAnswers()) {
            if (!isText(answerData.getAnswer()))
                return false;
        }

        return true;
    }

    /**
     * Метод проверяет параметр String text
     *
     * @return true - параметр String text содержат корректные данные и не равно пустате
     * false - параметр String text содержат не корректные данные или равно пустате
     */
    private boolean isText(String text) {
        String notDefined = TextParsing.NOT_DEFINED;
        if (text == null)
            return false;
        else if (text.equals(""))
            return false;
        else if (text.equals(notDefined))
            return false;
        else
            return true;
    }

    /**
     * Метод почищает все ненужные пробелы, табуляции
     *
     * @param text пердаваемый текст
     * @return готовый текст для дальшейшей обработки
     */
    private static String replaceAll(String text) {
        text = text.replaceAll("\t", "");
        text = text.replaceAll("    ", " ");
        text = text.replaceAll("   ", " ");
        text = text.replaceAll("  ", " ");
//        text = text.replaceAll("  ", " ");

        return text;
    }

    private static void getExceptionMessage(Exception ex) {
        System.out.println("ИСКЛЮЧЕНИЕ: " + ex + "\n" +
                "сообщение исключения: " + ex.getMessage());
    }

    private String parsingText(String text, int startRead, int finishRead) {
        String strReturn = "";
        StringBuilder sb = new StringBuilder();

        for (int i = startRead; i < finishRead; i++) {
            strReturn = sb.append(text.charAt(i)).toString();
        }

        return strReturn;
    }
}
