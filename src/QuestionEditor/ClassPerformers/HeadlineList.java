package QuestionEditor.ClassPerformers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by user on 04.10.2018.
 */
public class HeadlineList {
    private JPanel jPanel;
    private Component comp;
    private ArrayList<Component> listDescription;

    public HeadlineList(JPanel jPanel, Component comp) {
        this.jPanel = jPanel;
        this.comp = comp;
        this.listDescription = new ArrayList<>();
    }

    public Component getComp() {
        return comp;
    }

    public void setDescriptionClear() {
        if (this.listDescription != null) {
            for (Component component : listDescription) {
                jPanel.remove(component);
            }
            this.listDescription.clear();
        }
    }

    public void addComp(Component comp) {
        jPanel.add(comp);
        this.listDescription.add(comp);
    }

    public ArrayList<Component> getListDescription() {
        return this.listDescription;
    }

    @Override
    public String toString() {
        return "HeadlineList{" +
                "jPanel=" + jPanel +
                ",\n comp=" + comp +
                ",\n listDescription=" + listDescription.toString() +
                '}';
    }
}
