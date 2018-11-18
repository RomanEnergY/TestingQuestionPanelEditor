package QuestionEditor.ClassPerformers;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by user on 24.11.2017.
 */
public class MyJTextArea extends JTextArea implements MouseListener {
    private boolean right;
    private int sizeIntBOLD;
    private Font font;
    private Color colorEntered = new Color(180, 255, 175);
    private Color selectedColor = new Color(178, 181, 255);
    private Color colorExited;
    private Border actionBorder = BorderFactory.createLineBorder(new Color(0), 1);
    private boolean action = false; // Переменная отвечает за ативность объекта, при нажатии на объект
    private boolean isSelected = false;
    private boolean isVerifyCorrectnessOfAnswers = false;
    private Color wrongAnswerColor = new Color(0xFF0000);
    private Color rightAnswerColor = new Color(0x00FF00);

    public MyJTextArea(Font font, int sizeIntBOLD, String string_name, Color colorExited) {
        this(font, sizeIntBOLD, string_name, colorExited, false);
    }

    public MyJTextArea(Font font, int sizeIntBOLD, String string_name, Color colorExited, boolean wrap) {
        this(font, sizeIntBOLD, string_name, colorExited, wrap, false, false);
    }

    public MyJTextArea(Font font, int sizeIntBOLD, String string_name, Color colorExited, boolean wrap, boolean action, boolean right) {
        this.font = font;
        this.sizeIntBOLD = sizeIntBOLD;
        this.right = right;
        this.isSelected = false;
        this.action = action;
        this.colorExited = colorExited;
        this.setFont(font);
        this.setText(string_name);
        setLineWrap(wrap);
        setWrapStyleWord(true);
        setDisabledTextColor(Color.black); // Задаем цвет шрифта при отключении объекта
        setEnabled(false); // Отключаем объект, для предотвращения редактирования, обведения и копирования вопроса
        setBackground(this.colorExited);

        Border border = null;
        if (this.action) {
            this.addMouseListener(this);
            border = this.actionBorder;
        }

        this.setBorder(border);
    }

    public void setBorder(Border border) {
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(3, 3, 3, 3),
                BorderFactory.createCompoundBorder(
                        border,
                        BorderFactory.createEmptyBorder(3, 3, 3, 3))));
    }

    public void setBorderNull() {
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 3, 0, 3),
                BorderFactory.createCompoundBorder(
                        null,
                        BorderFactory.createEmptyBorder(0, 3, 0, 3))));
    }

    @Override
    public void setText(String t) {
        super.setText(t);
    }

    @Override
    public String toString() {
        return "\nMyJTextArea{} " + this.getText();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!this.isVerifyCorrectnessOfAnswers) {
            if (!this.isSelected)
                this.setBackground(this.selectedColor);
            else
                this.setBackground(this.colorEntered);

            this.isSelected = !this.isSelected;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!this.isSelected && !this.isVerifyCorrectnessOfAnswers) {
            this.setBackground(this.colorEntered);
            this.setFont(new Font(this.font.getName(), Font.BOLD, this.font.getSize()));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!this.isSelected && !this.isVerifyCorrectnessOfAnswers) {
            this.setBackground(this.colorExited);
            this.setFont(new Font(this.font.getName(), Font.PLAIN, this.font.getSize()));
        }
    }

    public void verifyColorCorrectnessOfAnswers() {
        if (!this.isVerifyCorrectnessOfAnswers) {
            if (this.isSelected) {
                if (this.right) {
                    this.setBackground(this.rightAnswerColor);
                } else {
                    this.setBackground(this.wrongAnswerColor);
                }
            } else if (this.right)
                this.setBackground(this.colorEntered);

            this.isVerifyCorrectnessOfAnswers = true;
        }
    }

    public boolean getCorrectnessOfAnswers() {
        if (this.right && this.isSelected) {
            return true;
        } else if (!this.right && this.isSelected) {
            return false;
        } else if (!this.isSelected && this.isRight()) {
            return false;
        }

        return true;
    }

    public boolean isRight() {
        return right;
    }

    /**
     * Метод проверяет выбранные ответы на правильность, выводя текст и его стиль
     * @param isText true выводит текст верности ответа
     */
    public void setTextCorrectnessOfAnswers(boolean isText) {
        if (this.isSelected) {
            this.setFont(new Font(this.font.getName(), Font.BOLD, this.font.getSize()));
            if (this.isRight()) {
                this.setFont(new Font(this.font.getName(), Font.BOLD, this.font.getSize()));
                this.setText(isText ? "(Верно) " + this.getText() : this.getText());
            } else {
                this.setFont(this.font);
                this.setFont(new Font(this.font.getName(), Font.PLAIN, this.font.getSize()));
                this.setText(isText ? "(НЕ верно) " + this.getText() : this.getText());
            }
        } else {
            this.setFont(new Font(this.font.getName(), Font.PLAIN, this.font.getSize()));
            if (this.isRight()) {
                this.setFont(new Font(this.font.getName(), Font.BOLD, this.font.getSize()));
                this.setText(isText ? "(Верно, не выбранно) " + this.getText() : this.getText());
            } else {
                this.setFont(new Font(this.font.getName(), Font.PLAIN, this.font.getSize()));
            }
        }

        this.isVerifyCorrectnessOfAnswers = true;
    }
}
