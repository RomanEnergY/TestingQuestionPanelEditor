package QuestionEditor.ClassPerformers;

import java.io.*;
import java.util.*;

/**
 * Created by user on 28.09.2018.
 */
public class QuestionEditor implements PerformersSetIsRight, ExitApplication {
    private boolean newDateNoSave = false;
    private String fileName = "Software setting.asop";
    private final int codeKey = 43;
    private final boolean isCodeKey = true;
    private HashSet<QuestionData> workedQuestionHashSet;
    private HashMap<QuestionData, ResponseLabel> questionDataResponseLabelHashMap;

    public QuestionEditor() {
        this.workedQuestionHashSet = new HashSet<>();
        this.questionDataResponseLabelHashMap = new HashMap<>();

        File file = new File(fileName);
        if (file.isFile()) {
            this.readerFileData();
        } else {
            String[] strings = new String[]{"Раздел 1.html", "Раздел 2.html", "Раздел 3.html"};
            ArrayList<QuestionData> questionDataArrayList = new ArrayList<>();

            for (String string : strings)
                questionDataArrayList.addAll(QuestionData.getCollectionQuestionDate(string));

            this.setMapResponseLabelNoAnswer(questionDataArrayList);
        }
    }

    public int getCountWorkedQuestion() {
        return this.workedQuestionHashSet.size();
    }

    public boolean isNewDateNoSave() {
        return newDateNoSave;
    }

    public void wantSaveData() {
        this.newDateNoSave = true;
    }


    HashMap<QuestionData, ResponseLabel> getQuestionDataResponseLabelHashMap() {
        return questionDataResponseLabelHashMap;
    }

    public ArrayList<QuestionData> getQuestionDataArrayList() {
        return new ArrayList<>(this.questionDataResponseLabelHashMap.keySet());
    }

    public ResponseLabel getResponseLabel(QuestionData questionData) {
        return this.getQuestionDataResponseLabelHashMap().get(questionData);
    }

    @Override
    public int getCountWorkedQuestionHashSet() {
        return this.workedQuestionHashSet.size();
    }

    private void setMapResponseLabelNoAnswer(ArrayList<QuestionData> responseLabelNoAnswer) {
        for (QuestionData questionData : responseLabelNoAnswer)
            this.questionDataResponseLabelHashMap.put(questionData, ResponseLabel.NO_ANSWER);
    }

    @Override
    // получение от ContentCenter данных при нажатии кнопки jbtCorrectnessOfAnswers
    public void setIsRight(QuestionData questionData, ResponseLabel responseLabel) {
        this.questionDataResponseLabelHashMap.put(questionData, responseLabel);
        this.workedQuestionHashSet.add(questionData);
    }

    private boolean writerFileData() {
        try {
            StringBuilder stringAllText;
            StringBuilder stringAnswers;
            StringBuilder stringWrite;

            FileWriter writer = new FileWriter(fileName, false);

            Iterator<Map.Entry<QuestionData, ResponseLabel>> entries = this.questionDataResponseLabelHashMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<QuestionData, ResponseLabel> entry = entries.next();

                stringAllText = new StringBuilder();
                stringWrite = new StringBuilder();
                stringAnswers = new StringBuilder();

                stringAllText.append(this.textAppend(entry.getKey().getDate(), ParsingConstant.FORMAT_DATE));
                stringAllText.append(this.textAppend(entry.getKey().getProgram(), ParsingConstant.FORMAT_PROGRAM));
                stringAllText.append(this.textAppend(entry.getKey().getSection(), ParsingConstant.FORMAT_SECTION));
                stringAllText.append(this.textAppend(String.valueOf(entry.getKey().getCodeQuestion()), ParsingConstant.FORMAT_CODE_QUESTION));
                stringAllText.append(this.textAppend(entry.getKey().getTextOfQuestion(), ParsingConstant.FORMAT_TEXT_OF_QUESTION));
                stringAllText.append(this.textAppend(entry.getKey().getReferenceBooks(), ParsingConstant.FORMAT_REFERENCE_BOOKS));
                stringAllText.append(this.textAppend(entry.getKey().getBook(), ParsingConstant.FORMAT_BOOKS));
                for (AnswerData answerData : entry.getKey().getAnswers()) {
                    stringAnswers.append(this.textAppend(answerData.getAnswer(), ParsingConstant.FORMAT_ANSWERS));
                    stringAnswers.append(this.textAppend(String.valueOf(answerData.isRight()), ParsingConstant.FORMAT_IS_RIGHT));
                }
                stringAllText.append(this.textAppend(stringAnswers.toString(), ParsingConstant.FORMAT_BLOCK_ANSWERS));
                stringAllText.append(this.textAppend(entry.getValue().toString(), ParsingConstant.FORMAT_RESPONSE_LABEL));
                stringWrite.append(this.textAppend(stringAllText.toString(), ParsingConstant.FORMAT_QUESTION_DATA));


                if (this.isCodeKey) {
                    String s = stringWrite.toString();
                    stringAllText = new StringBuilder();

                    for (int i = 0; i < s.length(); i++)
                            stringAllText.append((char) (s.charAt(i) + this.codeKey));
                }

//                System.out.println(stringAllText.toString());
                writer.write(stringAllText.toString());
                /*
                пример записи блока QuestionData в файл:
                <QUESTION_DATA><DATE>14.11.2017</DATE>
                <PROGRAM>Диспетчер РЭС</PROGRAM>
                <SECTION>Раздел 2 - Правила пожарной безопасности</SECTION>
                <CODE_QUESTION>14950</CODE_QUESTION>
                <TEXT_OF_QUESTION>В течение какого времени электрооборудование систем противопожарной защиты должно сохранять работоспособность в условиях пожара?</TEXT_OF_QUESTION>
                <REFERENCE_BOOKS>Федеральный закон № 123-ФЗ. Статья 143, п.4</REFERENCE_BOOKS>
                <BOOKS>СТО 34.01-27.1-001-2014</BOOKS>
                <BLOCK_ANSWERS><ANSWERS>Необходимого для полной эвакуации людей в безопасное место.</ANSWERS>
                <IS_RIGHT>true</IS_RIGHT>
                <ANSWERS>В течение всего времени развития и тушения пожара.</ANSWERS>
                <IS_RIGHT>false</IS_RIGHT>
                <ANSWERS>До прибытия подразделений пожарной охраны.</ANSWERS>
                <IS_RIGHT>false</IS_RIGHT>
                </BLOCK_ANSWERS>
                <RESPONSE_LABEL>NO_ANSWER</RESPONSE_LABEL>
                </QUESTION_DATA>
                 */
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }

    private String textAppend(String date, String[] formatDate) {
        return formatDate[0] + date + formatDate[1] + "\r\n";
    }

    public void readerFileData() {
        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (this.isCodeKey) {
                    for (int i = 0; i < line.length(); i++) {
                        stringBuilder.append((char) (line.charAt(i) - this.codeKey));
                    }

                    line = stringBuilder.toString();
                }

                this.questionDataResponseLabelHashMap.putAll(QuestionData.getParsToHashMap(line));

                stringBuilder = new StringBuilder();
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


    }

    @Override
    public boolean exit() {
        return this.writerFileData();
    }

    public int getNumberHoursWorkedIssues() {
        int numberHoursWorkedIssues = 0;
        Iterator<Map.Entry<QuestionData, ResponseLabel>> entries = this.questionDataResponseLabelHashMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<QuestionData, ResponseLabel> entry = entries.next();
            if (!entry.getValue().equals(ResponseLabel.NO_ANSWER))
                numberHoursWorkedIssues++;
        }

        return numberHoursWorkedIssues;
    }

    public int size() {
        return this.getQuestionDataResponseLabelHashMap().size();
    }

    public int getCountCorrectAnswer() {
        int countCorrectAnswer = 0;

        Iterator<Map.Entry<QuestionData, ResponseLabel>> entries = this.questionDataResponseLabelHashMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<QuestionData, ResponseLabel> entry = entries.next();
            if (entry.getValue().equals(ResponseLabel.CORRECT_ANSWER))
                countCorrectAnswer++;
        }
        return countCorrectAnswer;
    }

    public int getCountWrongAnswer() {
        int countWrongAnswer = 0;

        Iterator<Map.Entry<QuestionData, ResponseLabel>> entries = this.questionDataResponseLabelHashMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<QuestionData, ResponseLabel> entry = entries.next();
            if (entry.getValue().equals(ResponseLabel.WRONG_ANSWER))
                countWrongAnswer++;
        }
        return countWrongAnswer;
    }
}
