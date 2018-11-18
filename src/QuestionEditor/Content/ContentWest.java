package QuestionEditor.Content;

import QuestionEditor.ClassPerformers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by user on 01.10.2018.
 */
public class ContentWest extends JPanel implements MouseMotionListener, FocusListener, LayoutManager {
    private static final int MINIMUM_SIZE_WIDTH = 200;
    private JTreeQuestion jTreeQuestion;
    private JScrollPane scrollPaneJTree1;
    private Color colorDefault;

    public ContentWest(QuestionEditor questionEditor, Font font) {
        this.jTreeQuestion = new JTreeQuestion(questionEditor, font);
        scrollPaneJTree1 = new JScrollPane(this.jTreeQuestion, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.setLayout(this);

        this.add(scrollPaneJTree1);

        this.addMouseMotionListener(this);
        this.addFocusListener(this);
        this.colorDefault = this.getBackground();
    }



    public void setJTreeSubmitQuestion(SubmitQuestion submitQuestion) {
        this.jTreeQuestion.setSubmitQuestion(submitQuestion);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (this.getCursor() == Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)) {
            if (e.getX() > MINIMUM_SIZE_WIDTH) {
                this.setPreferredSize(new Dimension(e.getX(), this.getPreferredSize().height));
                this.scrollPaneJTree1.setPreferredSize(new Dimension(e.getX() - 10, this.scrollPaneJTree1.getPreferredSize().height));

                revalidate();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.requestFocusInWindow();
        if (this.getWidth() < e.getX() + 5) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            this.setBackground(new Color(221, 193, 255));
        } else {
            this.setCursor(Cursor.getDefaultCursor());
            this.setBackground(this.colorDefault);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        this.setCursor(Cursor.getDefaultCursor());
        this.setBackground(this.colorDefault);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(400, 100);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 100);
    }

    @Override
    public void layoutContainer(Container parent) {
        scrollPaneJTree1.setBounds(0,0,parent.getWidth() - 5, parent.getHeight());
    }
}
