package QuestionEditor.ClassPerformers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by user on 17.10.2018.
 */
public class FormModal extends JDialog {
    private boolean resultDialog;

    public boolean isOK(){
        return resultDialog;
    }
    public FormModal(JFrame owner){
        super(owner);
        setSize(400,300);
        setLocationRelativeTo(owner);
        setModalityType(ModalityType.TOOLKIT_MODAL);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JButton button1 = new JButton("OK");
        setLayout(new FlowLayout());
        add(button1);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultDialog = true;
                FormModal.this.setVisible(false);
            }
        });
        setVisible(true);
    }
}
