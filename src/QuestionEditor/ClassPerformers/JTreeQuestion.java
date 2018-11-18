package QuestionEditor.ClassPerformers;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.*;

/**
 * Created by user on 05.02.2018.
 */
public class JTreeQuestion extends JTree implements MouseMotionListener {
    private SubmitQuestion submitQuestion; // интерфейс реализующий "Передать вопрос"
    private Icon iconCloseExpanded;
    private Icon iconOpenExpanded;
    private Icon iconCloseExpandedPlus;
    private Icon iconCloseExpandedMinus;
    private Icon iconLeafDefault;
    private Icon iconCorrectAnswer;
    private Icon iconWrongAnswer;

    public JTreeQuestion(QuestionEditor questionEditor, Font font) {
        this.setFont(new Font(font.getName(), Font.PLAIN, font.getSize() - 8));
        this.addMouseMotionListener(this);
        this.submitQuestion = null;

        this.iconLeafDefault = new ImageIcon(getClass().getResource("/QuestionEditor/img/information-octagon-frame.png"));
        this.iconCorrectAnswer = new ImageIcon(getClass().getResource("/QuestionEditor/img/tick-octagon-frame.png"));
        this.iconWrongAnswer = new ImageIcon(getClass().getResource("/QuestionEditor/img/cross-octagon-frame.png"));
        this.iconCloseExpanded = new ImageIcon(getClass().getResource("/QuestionEditor/img/folder.png"));
        this.iconCloseExpandedPlus = new ImageIcon(getClass().getResource("/QuestionEditor/img/folder--plus.png"));
        this.iconCloseExpandedMinus = new ImageIcon(getClass().getResource("/QuestionEditor/img/folder--minus.png"));
        this.iconOpenExpanded = new ImageIcon(getClass().getResource("/QuestionEditor/img/folder-open.png"));

        this.setModel(createTreeModelTest(questionEditor.getQuestionDataArrayList())); // Создание модели дерева
        this.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            /* tree - это JTree в приемник настроено для
             * value - значение текущего дерева клетку value
             * selected true, то ячейки будут отображаться как если выбирать
             * expanded это истинный узел, в настоящее время расширяется
             * leaf это истинный узел представляет лист
             * hasFocus это истинный узел в настоящий момент внимание
             */
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                if (leaf) {
                    TreePath treePath = tree.getPathForRow(row);
                    if (treePath != null) {
                        Iterator<Map.Entry<QuestionData, ResponseLabel>> entries = questionEditor.getQuestionDataResponseLabelHashMap().entrySet().iterator();
                        while (entries.hasNext()) {
                            Map.Entry<QuestionData, ResponseLabel> entry = entries.next();
                            if (treePath.getLastPathComponent().toString().equals(String.valueOf(entry.getKey().getCodeQuestion())))
                                if (treePath.getPathComponent(treePath.getPathCount() - 2).toString().equals(entry.getKey().getBook()))
                                    if (treePath.getPathComponent(treePath.getPathCount() - 3).toString().equals(entry.getKey().getSection())) {
                                        setIcon(getIconResponseLabel(entry.getValue()));
                                        return component;
                                    }
                        }
                    }
                } else

                {
                    if (expanded) {
                        // Если раскрыта папка
                        if (iconOpenExpanded != null)
                            setIcon(iconOpenExpanded);
                    } else {
                        // Если не раскрыта папка
                        if (tree.getPathForRow(row) != null) {
                            if (tree.getPathForRow(row).getPath().length > 1) {
                                if (this.isIconCloseExpanded(tree.getPathForRow(row))) {
                                    if (iconCloseExpandedPlus != null)
                                        setIcon(iconCloseExpandedPlus);
                                } else {
                                    if (iconCloseExpandedMinus != null)
                                        setIcon(iconCloseExpandedMinus);
                                }
                            } else {
                                if (iconCloseExpanded != null)
                                    setIcon(iconCloseExpanded);
                            }
                        }

                    }
                }

                return component;
            }

            private boolean isIconCloseExpanded(TreePath pathForRow) {
                boolean isIconCloseExpanded = false;

                if (pathForRow != null) {
                    Iterator<Map.Entry<QuestionData, ResponseLabel>> entries = questionEditor.getQuestionDataResponseLabelHashMap().entrySet().iterator();
                    switch (pathForRow.getPath().length) {
                        case 1: // pathForRow=[treeNodeRoot]
                            return false;

                        case 2: // pathForRow=[treeNodeRoot, treeNodeProgram]
                            while (entries.hasNext()) {
                                Map.Entry<QuestionData, ResponseLabel> entry = entries.next();
                                if (pathForRow.getLastPathComponent().toString().equals(entry.getKey().getProgram())) {
                                    if (entry.getValue() != ResponseLabel.CORRECT_ANSWER)
                                        return false;
                                }
                            }
                            isIconCloseExpanded = true;
                            break;

                        case 3: // pathForRow=[treeNodeRoot, treeNodeProgram, treeNodeSection]
                            while (entries.hasNext()) {
                                Map.Entry<QuestionData, ResponseLabel> entry = entries.next();
                                if (pathForRow.getLastPathComponent().toString().equals(entry.getKey().getSection())) {
                                    if (entry.getValue() != ResponseLabel.CORRECT_ANSWER)
                                        return false;
                                }
                            }
                            isIconCloseExpanded = true;
                            break;

                        case 4: // pathForRow=[treeNodeRoot, treeNodeProgram, treeNodeSection, treeNodeBook]
                            while (entries.hasNext()) {
                                Map.Entry<QuestionData, ResponseLabel> entry = entries.next();
                                if (pathForRow.getLastPathComponent().toString().equals(entry.getKey().getBook())) {
                                    if (entry.getValue() != ResponseLabel.CORRECT_ANSWER)
                                        return false;
                                }
                            }
                            isIconCloseExpanded = true;
                            break;
                    }
                }

                // TODO можно добавить событие об ответе на блок вопросов
                return isIconCloseExpanded;
            }
        });

        this.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object[] objects = e.getPath().getPath();

                // Проверяем, если выбран код вопроса, тогда передаем данные в панель
                boolean b = false;
                if (objects.length == 5) { // Проверяем кол-во вложений в дерево
                    for (QuestionData questionData : questionEditor.getQuestionDataArrayList()) {
                        if (objects[1].toString().equals(questionData.getProgram())) {
                            if (objects[2].toString().equals(questionData.getSection())) {
                                if (objects[3].toString().equals(questionData.getBook())) {
                                    if (objects[4].toString().equals(String.valueOf(questionData.getCodeQuestion()))) {
                                        if (submitQuestion != null) {
//                                            System.out.println(e.getPath());
//                                            System.out.println(questionData);
//                                            submitQuestion.setQuestionData(e.getPath(), questionData, entry.getValue());
                                        }
                                        b = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (objects.length == 5) { // Проверяем кол-во вложений в дерево
                    Iterator<Map.Entry<QuestionData, ResponseLabel>> entries = questionEditor.getQuestionDataResponseLabelHashMap().entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<QuestionData, ResponseLabel> entry = entries.next();
                        if (objects[1].toString().equals(String.valueOf(entry.getKey().getProgram())))
                            if (objects[2].toString().equals(entry.getKey().getSection()))
                                if (objects[3].toString().equals(entry.getKey().getBook()))
                                    if (objects[4].toString().equals(String.valueOf(entry.getKey().getCodeQuestion()))) {
//                                        System.out.println("entry.getKey():" + entry.getKey());
                                        submitQuestion.setQuestionData(e.getPath(), entry.getKey(), entry.getValue());
                                    }
                    }
                }


                if (!b) {
                    if (submitQuestion != null) {
                        submitQuestion.setQuestionData(e.getPath(), null, null);
                    }
                }
            }
        });


    }

    public Icon getIconResponseLabel(ResponseLabel responseLabel) {
        Icon icon = null;
        switch (responseLabel) {
            case NO_ANSWER:
                icon = iconLeafDefault;
                break;
            case WRONG_ANSWER:
                icon = iconWrongAnswer;
                break;
            case CORRECT_ANSWER:
                icon = iconCorrectAnswer;
                break;
        }
        return icon;
    }


    /**
     * Метод создает дерево на остновании ArrayList<QuestionData> questionDates
     *
     * @param questionDates
     * @return
     */

    private TreeModel createTreeModelTest(ArrayList<QuestionData> questionDates) {
        // Сортировка массива данных
        goSortDateJFree_CodeQuestion(questionDates);
        goSortDateJFree_Books(questionDates);
        goSortDateJFree_Section(questionDates);
        goSortDateJFree_Program(questionDates);

        // формирование дерева JFree по средством ссылки на DefaultMutableTreeNode
        String s = "Программа"; // 1 уровень
        DefaultMutableTreeNode treeNodeRoot = new DefaultMutableTreeNode(s); // Верхний уровень
        DefaultMutableTreeNode treeNodeProgram = null;
        DefaultMutableTreeNode treeNodeSection = null;
        DefaultMutableTreeNode treeNodeBook = null;
        DefaultMutableTreeNode treeNodeCodeQuestion = null;

        for (int i = 0; i < questionDates.size(); i++) {
            treeNodeProgram = createDefaultMutableTreeNode(treeNodeRoot, questionDates.get(i).getProgram());
            treeNodeSection = createDefaultMutableTreeNode(treeNodeProgram, questionDates.get(i).getSection());
            treeNodeBook = createDefaultMutableTreeNode(treeNodeSection, questionDates.get(i).getBook());
            treeNodeCodeQuestion = createDefaultMutableTreeNode(treeNodeBook, String.valueOf(questionDates.get(i).getCodeQuestion()));
        }

        return new DefaultTreeModel(treeNodeRoot);
    }

    /**
     * Метод добавляет элемент в дерево
     *
     * @param treeNodeRoot
     * @param text
     * @return
     */
    private DefaultMutableTreeNode createDefaultMutableTreeNode(DefaultMutableTreeNode treeNodeRoot, String text) {
        Enumeration objectEnumeration = treeNodeRoot.children();
        DefaultMutableTreeNode treeNodeTemp = null;
        DefaultMutableTreeNode treeNodeReturn = null;

        // Опеределяем ссылку на элемент
        while (objectEnumeration.hasMoreElements()) {
            treeNodeTemp = (DefaultMutableTreeNode) objectEnumeration.nextElement();
            if (treeNodeTemp.toString().equals(text)) {
                treeNodeReturn = treeNodeTemp;
                break;
            }
        }
        if (treeNodeReturn == null) {
            // добавляем в конец перечисления
            treeNodeRoot.add(new DefaultMutableTreeNode(text));

            // Определяем последнее добавленные данны
            objectEnumeration = treeNodeRoot.children();
            while (objectEnumeration.hasMoreElements()) {
                treeNodeTemp = (DefaultMutableTreeNode) objectEnumeration.nextElement();
                if (treeNodeTemp.toString().equals(text)) {
                    treeNodeReturn = treeNodeTemp;
                    break;
                }
            }
        }
        return treeNodeReturn;
    }

    private void goSortDateJFree_Program(ArrayList<QuestionData> questionDate) {
        Collections.sort(questionDate, new Comparator<QuestionData>() {
            @Override
            public int compare(QuestionData o1, QuestionData o2) {
                int p1_compareTo = o1.getProgram().compareTo(o2.getProgram());
                return p1_compareTo;
            }
        });
    }

    private void goSortDateJFree_Section(ArrayList<QuestionData> questionDate) {
        Collections.sort(questionDate, new Comparator<QuestionData>() {
            @Override
            public int compare(QuestionData o1, QuestionData o2) {
                int p1_compareTo = o1.getSection().compareTo(o2.getSection());
                return p1_compareTo;
            }
        });
    }

    private void goSortDateJFree_Books(ArrayList<QuestionData> questionDates) {
        Collections.sort(questionDates, new Comparator<QuestionData>() {
            @Override
            public int compare(QuestionData o1, QuestionData o2) {
                int p1_compareTo = o1.getBook().compareTo(o2.getBook());
                return p1_compareTo;
            }
        });
    }

    private void goSortDateJFree_CodeQuestion(ArrayList<QuestionData> questionDate) {
        Collections.sort(questionDate, new Comparator<QuestionData>() {
            @Override
            public int compare(QuestionData o1, QuestionData o2) {
                int p1_compareTo = o1.getCodeQuestion() - o2.getCodeQuestion();
                return p1_compareTo;
            }
        });
    }

    private void goSortDateJFree_ReferenceBooks(ArrayList<QuestionData> questionDates) {
        Collections.sort(questionDates, new Comparator<QuestionData>() {
            @Override
            public int compare(QuestionData o1, QuestionData o2) {
                int p1_compareTo = o1.getReferenceBooks().compareTo(o2.getReferenceBooks());
                return p1_compareTo;
            }
        });
    }

    public void setSubmitQuestion(SubmitQuestion submitQuestion) {
        this.submitQuestion = submitQuestion;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.requestFocusInWindow();
    }
}
