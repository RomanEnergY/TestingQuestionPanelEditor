package QuestionEditor.Content;

import QuestionEditor.ClassPerformers.AnswerData;
import QuestionEditor.ClassPerformers.*;
import QuestionEditor.ClassPerformers.QuestionData;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by user on 01.10.2018.
 */
public class ContentCenter extends JPanel implements SubmitQuestion, MouseMotionListener, ActionListener {
    private PerformersSetIsRight performersSetIsRight;
    private final Font font;
    private int sizeIntBOLD;
    private QuestionData actionQuestionData;
    private ResponseLabel actionResponseLabel;
    private Color background;

    private Headline headlineCodeQuestion;
    private HeadlineList headlineTextQuestion;
    private Headline headlineAnswerIsRight;
    private HeadlineList headlineAllAnswer;

    private JCheckBox jCheckBox;
    private JButton jbtCorrectnessOfAnswers = new JButton("Проверить правильность ответов");
    private JButton jbtResetAnswers = new JButton("Сбросить ответы");
    private Color backgroundJBT;
    private TreePath treePath;
    private ChangeListener changeListener;
    private ExitApplication exitApplication;

    public ContentCenter(Font font, int sizeIntBOLD, Color background) {
//        this.questionDataResponseLabelHashMap = new HashMap<>();
        this.font = font;
        this.sizeIntBOLD = sizeIntBOLD;
        this.backgroundJBT = jbtCorrectnessOfAnswers.getBackground();
        this.background = background;
        this.setBackground(background);
        this.setLayout(new LayoutJPanelCenter());

        this.jCheckBox = new JCheckBox("Выводить правильные ответы");
        this.jCheckBox.setFont(this.font);
        this.jCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox jCheckBox = (JCheckBox) e.getSource();
                if (jCheckBox.isSelected()) {
                    deduceCorrectnessOfAnswers(headlineAllAnswer, false);
                    jbtCorrectnessOfAnswers.setEnabled(false);
                    jbtResetAnswers.setVisible(true);
                }
                else {
                    setQuestionData(treePath, actionQuestionData, actionResponseLabel);
                    jbtCorrectnessOfAnswers.setEnabled(true);
                    jbtResetAnswers.setVisible(false);
                }
            }
        });
        this.add(this.jCheckBox);

        this.headlineCodeQuestion = new Headline(this, new MyJTextArea(font, sizeIntBOLD, "Вопрос №", background, false));
//        this.headlineCodeQuestion = new Headline(this, new JTextArea("Вопрос №"));

        MyJTextArea myJTextArea = new MyJTextArea(font, sizeIntBOLD, "Текст вопроса:", background, false);
        myJTextArea.setBorderNull();
        this.headlineTextQuestion = new HeadlineList(this, myJTextArea);

//        this.headlineTextQuestion = new Headline(this, new JTextArea("Всего ответов:"));
        this.headlineAnswerIsRight = new Headline(this, new MyJTextArea(font, sizeIntBOLD, "Всего ответов ", background, false));
        this.headlineAllAnswer = new HeadlineList(this, new MyJTextArea(font, sizeIntBOLD, "Ответы:", background, false));

        this.add(this.headlineCodeQuestion.getComp());
        this.add(this.headlineTextQuestion.getComp());
        this.add(this.headlineAnswerIsRight.getComp());
        this.add(this.headlineAllAnswer.getComp());
        this.add(this.jbtCorrectnessOfAnswers);
        this.add(this.jbtResetAnswers);

        for (Component component : this.getComponents()) {
            component.setVisible(false);
        }

        this.addMouseMotionListener(this);

        this.jbtCorrectnessOfAnswers.addActionListener(this);

        this.jbtResetAnswers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jCheckBox.isSelected()) {
                    jCheckBox.setSelected(false);
                }
                setQuestionData(treePath, actionQuestionData, actionResponseLabel);
            }
        });
    }

    public void setPerformersSetIsRight(PerformersSetIsRight performersSetIsRight) {
        this.performersSetIsRight = performersSetIsRight;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void setQuestionData(TreePath treePath, QuestionData actionQuestionData, ResponseLabel value) {
        if (actionQuestionData != null) {
            this.treePath = treePath;
            this.actionQuestionData = actionQuestionData;
//            for (Component component : this.getComponents()) {
//                if (!component.isVisible())
//                    component.setVisible(true);
//            }

            // Вопрос №
            this.headlineCodeQuestion.setDescriptionClear();
            this.headlineCodeQuestion.setComp(new MyJTextArea(new Font(this.font.getName(), Font.BOLD, this.font.getSize() + sizeIntBOLD), sizeIntBOLD, String.valueOf(this.actionQuestionData.getCodeQuestion()), background, true));

            // Текст вопроса:
            this.headlineTextQuestion.setDescriptionClear();
            MyJTextArea myJTextArea = new MyJTextArea(new Font(this.font.getName(), Font.BOLD, this.font.getSize() + sizeIntBOLD), sizeIntBOLD, this.actionQuestionData.getTextOfQuestion(), background, true);
            myJTextArea.setBorderNull();
            this.headlineTextQuestion.addComp(myJTextArea);
            myJTextArea = new MyJTextArea(new Font(this.font.getName(), Font.PLAIN, this.font.getSize() - sizeIntBOLD), sizeIntBOLD, '(' + this.actionQuestionData.getReferenceBooks() + ')', background, true);
            myJTextArea.setBorderNull();
            this.headlineTextQuestion.addComp(myJTextArea);

//            this.headlineTextQuestion.setComp(new MyJTextArea(this.actionQuestionData.getTextOfQuestion() + "\n(" + this.actionQuestionData.getReferenceBooks() + ")", background, true));

            // Количество правильных и ответов всего:
            this.headlineAnswerIsRight.setDescriptionClear();
            this.headlineAnswerIsRight.setComp(new MyJTextArea(font, sizeIntBOLD, String.valueOf(this.actionQuestionData.getCountAnswer() + ", из них правильных " + this.actionQuestionData.getCountAnswerIsRight()), background, true));

            // Список ответы:
            this.headlineAllAnswer.setDescriptionClear();
            for (AnswerData answerData : this.actionQuestionData.getShuffleAnswers()) {
                this.headlineAllAnswer.addComp(new MyJTextArea(font, sizeIntBOLD, answerData.getAnswer(), background, true, true, answerData.isRight()));
            }

            this.jbtCorrectnessOfAnswers.setBackground(backgroundJBT);
            this.jbtCorrectnessOfAnswers.setEnabled(true);


            this.getLayout().layoutContainer(this);
//            this.jbtCorrectnessOfAnswers.setVisible(true);



            if (this.jCheckBox.isSelected()) {
                for (Component component : this.getComponents()) {
                    if (component.isVisible())
                        component.setVisible(false);
                }

                this.jCheckBox.setSelected(false);
                this.jCheckBox.doClick();

                for (Component component : this.getComponents()) {
                    if (!component.isVisible())
                        component.setVisible(true);
                }

            } else {
                for (Component component : this.getComponents()) {
                    if (!component.isVisible())
                        component.setVisible(true);
                }

                this.jbtResetAnswers.setVisible(false);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (headlineAllAnswer.getListDescription() != null) {
            if (!this.jCheckBox.isSelected()) {
                if (this.deduceCorrectnessOfAnswers(headlineAllAnswer, true)) {
                    jbtCorrectnessOfAnswers.setBackground(Color.GREEN);
                    if (this.performersSetIsRight != null)
                        this.performersSetIsRight.setIsRight(actionQuestionData, ResponseLabel.CORRECT_ANSWER);
                } else {
                    jbtCorrectnessOfAnswers.setBackground(Color.RED);
                    if (this.performersSetIsRight != null)
                        this.performersSetIsRight.setIsRight(actionQuestionData, ResponseLabel.WRONG_ANSWER);
                }

                jbtCorrectnessOfAnswers.setEnabled(false);
                jbtResetAnswers.setVisible(true);

                // Передаем событие изменения данных в коллекции вопросов, для подсчета статистики
                if (this.changeListener != null && this.performersSetIsRight != null) {
                    this.changeListener.stateChanged(new ChangeEvent(this.performersSetIsRight.getCountWorkedQuestionHashSet()));
                }

                // передаем событие, необходимости при завершении программы сохранить данные
                if (this.exitApplication != null) {
                    this.exitApplication.exit();
                }
            }
        }
    }

    private boolean deduceCorrectnessOfAnswers(HeadlineList headlineAllAnswer, boolean isText) {
        MyJTextArea myJTextAreaTemp;
        boolean b = true;
        for (int i = 0; i < headlineAllAnswer.getListDescription().size(); i++) {
            myJTextAreaTemp = (MyJTextArea) headlineAllAnswer.getListDescription().get(i);
            myJTextAreaTemp.verifyColorCorrectnessOfAnswers();
            myJTextAreaTemp.setTextCorrectnessOfAnswers(isText);

            if (b) {
                b = myJTextAreaTemp.getCorrectnessOfAnswers();
            }
        }

        return b;
    }

    public void setExitApplication(ExitApplication exitApplication) {
        this.exitApplication = exitApplication;
    }

    class LayoutJPanelCenter implements LayoutManager {

        @Override
        /**
         * Если менеджер компоновки использует строку на компонент,
         * добавляет компонент <code>comp</code> на макет,
         * связав его со строкой, определенной <code>name</code>.
         *
         * @param name строки, которая будет связана с компонентом
         * @param comp компонент, который будет добавлен
         */
        public void addLayoutComponent(String name, Component comp) {

        }

        @Override
        /**
         * Удаляет указанный компонент из макета.
         * @param comp компонент должен быть удален
         */
        public void removeLayoutComponent(Component comp) {
            repaint();
        }

        @Override
        /**
         * Вычисляет предпочтительные размеры размера для указанного
         * контейнер, учитывая компоненты, которые он содержит.
         * @param parent контейнера должны быть изложены
         * @see #minimumLayoutSize
         */
        public Dimension preferredLayoutSize(Container parent) {
            return null;
        }

        @Override
        /**
         * Вычисляет размеры минимального размера для указанного контейнера,
         * учитывая содержащиеся в нем компоненты.
         * @param parent компонент, котор нужно положить вне
         * @see #preferredLayoutSize
         */
        public Dimension minimumLayoutSize(Container parent) {
            return null;
        }

        @Override
        /**
         * Раскладывает указанный контейнер.
         * @param родительского контейнера должны быть изложены
         */
        public void layoutContainer(Container parent) {
            Component componentLast = null;
            Point startPoint = new Point(5, 5);
            int retreatY = 20;

            // Вопрос №
            headlineCodeQuestion.getComp().setBounds(
                    startPoint.x,
                    startPoint.y,
                    (int) headlineCodeQuestion.getComp().getPreferredSize().getWidth(),
                    (int) headlineCodeQuestion.getComp().getPreferredSize().getHeight()
            );
            if (headlineCodeQuestion.getDescription() != null) {
                headlineCodeQuestion.getDescription().setBounds(
                        (int) headlineCodeQuestion.getComp().getBounds().getMaxX(),
                        (int) headlineCodeQuestion.getComp().getBounds().getY(),
                        parent.getBounds().width - (int) headlineCodeQuestion.getComp().getBounds().getMaxX() - startPoint.x * 2,
                        (int) headlineCodeQuestion.getDescription().getPreferredSize().getHeight()
                );
            }

            // jCheckBox
            jCheckBox.setBounds(
                    parent.getWidth() - (int) jCheckBox.getPreferredSize().getWidth(),
                    headlineCodeQuestion.getComp().getBounds().y,
                    (int) jCheckBox.getPreferredSize().getWidth(),
                    (int) jCheckBox.getPreferredSize().getHeight()
            );

            // Текст вопроса:
            if (headlineCodeQuestion.getDescription() == null)
                headlineTextQuestion.getComp().setLocation(new Point(startPoint.x, (int) headlineCodeQuestion.getComp().getBounds().getMaxY()));
            else
                headlineTextQuestion.getComp().setLocation(new Point(startPoint.x, (int) headlineCodeQuestion.getDescription().getBounds().getMaxY()));

            headlineTextQuestion.getComp().setBounds(
                    headlineTextQuestion.getComp().getLocation().x,
                    headlineTextQuestion.getComp().getLocation().y + retreatY,
                    (int) headlineTextQuestion.getComp().getPreferredSize().getWidth(),
                    (int) headlineTextQuestion.getComp().getPreferredSize().getHeight());
            componentLast = null;
            if (headlineTextQuestion.getListDescription() != null) {
                for (Component component : headlineTextQuestion.getListDescription()) {
                    if (componentLast == null) {
                        component.setBounds(
                                (int) headlineTextQuestion.getComp().getBounds().getMaxX(),
                                (int) headlineTextQuestion.getComp().getBounds().getY(),
                                parent.getBounds().width - (int) headlineTextQuestion.getComp().getBounds().getMaxX() - startPoint.x * 2,
                                (int) component.getPreferredSize().getHeight());
                        componentLast = component;
                    } else {
                        component.setBounds(
                                (int) componentLast.getBounds().getX(),
                                (int) componentLast.getBounds().getMaxY(),
                                (int) componentLast.getBounds().getWidth(),
                                (int) component.getPreferredSize().getHeight());
                        componentLast = component;
                    }
                }
            }

            // Количество правильных и ответов всего:
            if (componentLast == null)
                headlineAnswerIsRight.getComp().setLocation(new Point(startPoint.x, (int) headlineTextQuestion.getComp().getBounds().getMaxY()));
            else
                headlineAnswerIsRight.getComp().setLocation(new Point(startPoint.x, (int) componentLast.getBounds().getMaxY()));

            headlineAnswerIsRight.getComp().setBounds(
                    headlineAnswerIsRight.getComp().getLocation().x,
                    headlineAnswerIsRight.getComp().getLocation().y + retreatY,
                    (int) headlineAnswerIsRight.getComp().getPreferredSize().getWidth(),
                    (int) headlineAnswerIsRight.getComp().getPreferredSize().getHeight());
            if (headlineAnswerIsRight.getDescription() != null) {
                headlineAnswerIsRight.getDescription().setBounds(
                        (int) headlineAnswerIsRight.getComp().getBounds().getMaxX(),
                        (int) headlineAnswerIsRight.getComp().getBounds().getY(),
                        parent.getBounds().width - (int) headlineAnswerIsRight.getComp().getBounds().getMaxX() - startPoint.x * 2,
                        (int) headlineAnswerIsRight.getDescription().getPreferredSize().getHeight());
            }

            // Список ответы:
            if (headlineAnswerIsRight.getDescription() == null)
                headlineAllAnswer.getComp().setLocation(new Point(startPoint.x, (int) headlineAnswerIsRight.getComp().getBounds().getMaxY()));
            else
                headlineAllAnswer.getComp().setLocation(new Point(startPoint.x, (int) headlineAnswerIsRight.getDescription().getBounds().getMaxY()));


            headlineAllAnswer.getComp().setBounds(
                    headlineAllAnswer.getComp().getLocation().x,
                    headlineAllAnswer.getComp().getLocation().y + retreatY,
                    (int) headlineAllAnswer.getComp().getPreferredSize().getWidth(),
                    (int) headlineAllAnswer.getComp().getPreferredSize().getHeight());
            componentLast = null;
            if (headlineAllAnswer.getListDescription() != null) {
                for (Component component : headlineAllAnswer.getListDescription()) {
                    if (componentLast == null) {
                        component.setBounds(
                                (int) headlineAllAnswer.getComp().getBounds().getMaxX(),
                                (int) headlineAllAnswer.getComp().getBounds().getY(),
                                parent.getBounds().width - (int) headlineAllAnswer.getComp().getBounds().getMaxX() - startPoint.x * 2,
                                (int) component.getPreferredSize().getHeight());
                        componentLast = component;
                    } else {
                        component.setBounds(
                                (int) componentLast.getBounds().getX(),
                                (int) componentLast.getBounds().getMaxY(),
                                (int) componentLast.getBounds().getWidth(),
                                (int) component.getPreferredSize().getHeight());
                        componentLast = component;
                    }
                }
            }

            // установка кнопки
            if (componentLast == null)
                jbtCorrectnessOfAnswers.setLocation(new Point(startPoint.x, (int) headlineAnswerIsRight.getComp().getBounds().getMaxY()));
            else
                jbtCorrectnessOfAnswers.setLocation(new Point(startPoint.x, (int) componentLast.getBounds().getMaxY()));

            jbtCorrectnessOfAnswers.setBounds(
                    jbtCorrectnessOfAnswers.getLocation().x,
                    jbtCorrectnessOfAnswers.getLocation().y + retreatY,
                    (int) jbtCorrectnessOfAnswers.getPreferredSize().getWidth(),
                    (int) jbtCorrectnessOfAnswers.getPreferredSize().getHeight());

            jbtResetAnswers.setBounds(
                    (int) jbtCorrectnessOfAnswers.getBounds().getMaxX() + retreatY,
                    jbtCorrectnessOfAnswers.getLocation().y,
                    (int) jbtResetAnswers.getPreferredSize().getWidth(),
                    (int) jbtResetAnswers.getPreferredSize().getHeight());
        }
    }
}
