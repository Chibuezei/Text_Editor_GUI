package editor;

import javax.swing.*;

public class TextEditor extends JFrame {
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 300;
    public TextEditor() {
        super("TextEditor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        initComponents();
        setVisible(true);
        setLayout(null);
    }

    private void initComponents() {
        JTextArea textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setBounds(40, 150, 220, 70);
        add(textArea);

    }


}
