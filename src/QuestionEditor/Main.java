package QuestionEditor;

import QuestionEditor.ClassPerformers.ExitApplication;
import QuestionEditor.ClassPerformers.QuestionEditor;
import QuestionEditor.Content.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Created by user on 28.09.2018.
 */
public class Main implements ExitApplication {
    private QuestionEditor questionEditor;
    private ExitApplication exitApplication;
    private boolean exitNewDate = false;

    public static void main(String[] args) {
        // Создаем отдельный поток и запускаем проект
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }

    private Main() {
        this.questionEditor = new QuestionEditor();
        Font font = new Font("Times New Roman", Font.PLAIN, 25);
        int sizeIntBOLD = 8;

        JFrame mainJFrame = new JFrame(this.getClass().toString());
        mainJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                UIManager.put("OptionPane.yesButtonText", "Да");
                UIManager.put("OptionPane.noButtonText", "Нет");
                UIManager.put("OptionPane.cancelButtonText", "Отмена");

                int result = JOptionPane.showConfirmDialog(mainJFrame, "Закрыть приложение?", "Окно подтверждения", JOptionPane.YES_NO_OPTION);
                // Окна подтверждения c 2-мя параметрами
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        if (exitNewDate) {
                            result = JOptionPane.showConfirmDialog(mainJFrame, "Данные об ответах сохранить?", "Окно подтверждения", JOptionPane.YES_NO_CANCEL_OPTION);
                            switch (result) {
                                case JOptionPane.YES_OPTION:
                                    if (exitApplication.exit())
                                        JOptionPane.showMessageDialog(mainJFrame, "Данные об ответах успешно сохранены");
                                    else
                                        JOptionPane.showMessageDialog(mainJFrame, "Ошибка: Данные об ответах НЕ сохранены");

                                    mainJFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//yes
                                    return;

                                case JOptionPane.NO_OPTION:
                                    JOptionPane.showMessageDialog(mainJFrame, "Данные об ответах не сохранены");
                                    mainJFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//yes
                                    return;

                                case JOptionPane.CANCEL_OPTION:
                                    mainJFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//cancel
                                    return;
                            }
                        }
                        mainJFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//yes
                        return;

                    case JOptionPane.NO_OPTION:
                        mainJFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//cancel
                }
            }
        });

        JPanel mainPanel = new JPanel();
        mainJFrame.add(mainPanel, BorderLayout.CENTER);

        mainPanel.setLayout(new BorderLayout());

        // Создание контента
        ContentNorth contentNorth = new ContentNorth(); // Север
        ContentEast contentEast = new ContentEast(); // Восток
        ContentSouth contentSouth = new ContentSouth(this.questionEditor, new Font(font.getName(), font.getStyle(), font.getSize())); // Юг
        ContentWest contentWest = new ContentWest(this.questionEditor, font); // Запад
        ContentCenter contentCenter = new ContentCenter(font, sizeIntBOLD, mainPanel.getBackground()); // Центр

        contentWest.setJTreeSubmitQuestion(contentCenter); // событие передачи вопроса из дерева в центральную панель для ответов
        contentCenter.setPerformersSetIsRight(this.questionEditor); // Событие передачи ответа из центральной консоли для определения значка в дереве данных
        contentCenter.setChangeListener(contentSouth); // событие изменения данных в коллекции вопросов
        contentCenter.setExitApplication(this);
        this.setExitApplication(this.questionEditor); // передаем событие закрытия приложения, для записи данных на диск


        mainPanel.add(contentNorth, BorderLayout.NORTH);
        mainPanel.add(contentEast, BorderLayout.EAST);
        mainPanel.add(contentSouth, BorderLayout.SOUTH);
        mainPanel.add(contentWest, BorderLayout.WEST);
        mainPanel.add(contentCenter, BorderLayout.CENTER);

        mainJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // при закрытии выйти и закрыть приложение
        mainJFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // развернуть на весь экран

//      Получаем разрешение экрана
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        mainJFrame.setMinimumSize(new Dimension((int) (dimension.width * 0.5), (int) (dimension.height * 0.5)));
        mainJFrame.setLocationRelativeTo(null); // Вызвать jFrame по центру экрана
        mainJFrame.setVisible(true);


    }

    public void setExitApplication(ExitApplication exitApplication) {
        this.exitApplication = exitApplication;
    }

    @Override
    public boolean exit() {
        exitNewDate = true;
        return false;
    }
}
