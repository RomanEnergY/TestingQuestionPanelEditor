package QuestionEditor.Content;

import QuestionEditor.ClassPerformers.QuestionEditor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by user on 01.10.2018.
 */
public class ContentSouth extends JPanel implements ChangeListener {
    private final Date date = new GregorianCalendar(2018, Calendar.NOVEMBER, 27).getTime();
    private QuestionEditor questionEditor;

    private static final String TEXT_JLab1 = "Вопросов в текущей сессии = ";
    private static final String TEXT_JLab2 = "Вопросов проработано итого = ";
    private static final String TEXT_JLab3 = "Всего вопросов = ";
    private static final String TEXT_JLab4 = "Процент проработанных = ";
    private static final String TEXT_JLab5 = "Правильные ответы = ";
    private static final String TEXT_JLab6 = "Неправильные ответы = ";
    private static final String TEXT_JLab7 = "Проверка знаний = ";
    private static final String TEXT_JLab8 = "Вопросов в день = ";

    private JLabel jLab1;
    private JLabel jLab2;
    private JLabel jLab3;
    private JLabel jLab4;
    private JLabel jLab5;
    private JLabel jLab6;
    private JLabel jLab7;
    private JLabel jLab8;

    public ContentSouth(QuestionEditor questionEditor, Font font) {
        font = new Font(font.getName(), font.getStyle(), font.getSize() - 6);
        this.questionEditor = questionEditor;
        this.jLab1 = new JLabel();
        this.jLab2 = new JLabel();
        this.jLab3 = new JLabel();
        this.jLab4 = new JLabel();
        this.jLab5 = new JLabel();
        this.jLab6 = new JLabel();
        this.jLab7 = new JLabel();
        this.jLab8 = new JLabel();

        this.stateChanged(null);
        this.setBackground(new Color(176, 163, 255));
        this.setLayout(new GridLayout(1, 0, 5, 5));

        JPanel jPanelLine1 = new JPanel();
        jPanelLine1.setBackground(this.getBackground());
        jPanelLine1.setLayout(new GridLayout(2, 0, 20, 5));
        jPanelLine1.add(this.jLab1);
        jPanelLine1.add(this.jLab2);

        JPanel jPanelLine2 = new JPanel();
        jPanelLine2.setBackground(this.getBackground());
        jPanelLine2.setLayout(new GridLayout(2, 0, 20, 5));
        jPanelLine2.add(this.jLab3);
        jPanelLine2.add(this.jLab4);

        JPanel jPanelLine3 = new JPanel();
        jPanelLine3.setBackground(this.getBackground());
        jPanelLine3.setLayout(new GridLayout(2, 0, 20, 5));
        jPanelLine3.add(this.jLab5);
        jPanelLine3.add(this.jLab6);

        JPanel jPanelLine4 = new JPanel();
        jPanelLine4.setBackground(this.getBackground());
        jPanelLine4.setLayout(new GridLayout(2, 0, 20, 5));
        jPanelLine4.add(this.jLab7);
        jPanelLine4.add(this.jLab8);

        this.jLab1.setFont(font);
        this.jLab2.setFont(font);
        this.jLab3.setFont(font);
        this.jLab4.setFont(font);
        this.jLab5.setFont(font);
        this.jLab6.setFont(font);
        this.jLab7.setFont(font);
        this.jLab8.setFont(font);

        this.add(jPanelLine1);
        this.add(jPanelLine2);
        this.add(jPanelLine3);
        this.add(jPanelLine4);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int count = 0;

        if (e != null) {
            if (e.getSource() instanceof Integer) {
                count = (Integer) e.getSource();
            }
        }

        Double d = (questionEditor.getNumberHoursWorkedIssues() * 100.0) / questionEditor.size();

        this.jLab1.setText(String.format("%s  %,4d", TEXT_JLab1, count));
        this.jLab2.setText(String.format("%s  %,4d", TEXT_JLab2, questionEditor.getNumberHoursWorkedIssues()));
        this.jLab3.setText(String.format("%s  %,4d", TEXT_JLab3, questionEditor.size()));
        this.jLab4.setText(String.format("%s  %,3.2f%s", TEXT_JLab4, d, "%"));
        this.jLab5.setText(String.format("%s  %,4d", TEXT_JLab5, questionEditor.getCountCorrectAnswer()));
        this.jLab6.setText(String.format("%s  %,4d", TEXT_JLab6, questionEditor.getCountWrongAnswer()));

        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        long difference = date.getTime() - gregorianCalendar.getTime().getTime();
        int days = (int) (difference / (24 * 60 * 60 * 1000)); // миллисекунды / (24ч * 60мин * 60сек * 1000мс)
        gregorianCalendar.setTime(date);
//        this.jLab7.setText(String.format("%s  %02d.%02d.%04d", TEXT_JLab7, gregorianCalendar.get(Calendar.DATE), gregorianCalendar.get(Calendar.MONTH), gregorianCalendar.get(Calendar.YEAR)));
        this.jLab7.setText("Проверка знаний: " + new SimpleDateFormat("dd MMMMMM yyyyг.").format(this.date.getTime()) + " (" + days + ") дней");
        ;
//        this.jLab8.setText(String.format("%s  %03d", TEXT_JLab8, days));

        d = (questionEditor.size() - questionEditor.getNumberHoursWorkedIssues()) / (double) days;
        this.jLab8.setText(String.format("%s  %,3.3f", TEXT_JLab8, d));
    }
}
