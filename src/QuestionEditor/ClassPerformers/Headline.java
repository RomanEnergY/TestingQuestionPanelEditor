package QuestionEditor.ClassPerformers;

import javax.swing.*;
import java.awt.*;

/**
 * Created by user on 04.10.2018.
 */
public class Headline {
    private JPanel jPanel;
    private Component comp;
    private Component description;

    public Headline(JPanel jPanel, Component comp) {
        this.jPanel = jPanel;
        this.comp = comp;
        this.description = null;
    }

    public Component getComp() {
        return comp;
    }

    public void setDescriptionClear() {
        if (this.description != null)
            jPanel.remove(description);

        this.description = null;
    }

    public void setComp(Component comp) {
        if (this.description == null) {
            jPanel.add(comp);
            this.description = comp;
        } else {
            setDescriptionClear();
            setComp(comp);
        }
    }

    public Component getDescription() {
        return description;
    }
}
