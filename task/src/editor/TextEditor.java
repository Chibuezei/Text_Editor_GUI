package editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TextEditor extends JFrame {
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 300;
    private static JTextField filename;
    private static JTextArea textArea;

    public TextEditor() {
        super("TextEditor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        initComponents();
        setVisible(true);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

    }

    private void initComponents() {
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setBounds(40, 150, 220, 70);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        int vertical = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int horizontal = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        JScrollPane scrollPane = new JScrollPane(textArea, vertical, horizontal);
        scrollPane.setName("ScrollPane");
        add(scrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
topPanel.setBackground(Color.GREEN);
        add(topPanel, BorderLayout.NORTH);


        filename = new JTextField();
        filename.setName("FilenameField");
        filename.setText("");
        filename.setPreferredSize(new Dimension(50, 25));
        setMargin(filename, 3, 3, 3, 3);
        filename.setBounds(10, 25, WINDOW_WIDTH - 100, 20);
        filename.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(filename);

        JButton saveButton = new JButton("SAVE");
        saveButton.setName("SaveButton");
        saveButton.addActionListener(e -> saveAction());
        topPanel.add(saveButton);


        JButton loadButton = new JButton("LOAD");
        loadButton.setName("LoadButton");
        loadButton.addActionListener(e -> loadAction());
        topPanel.add(loadButton);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setName("MenuLoad");
        loadMenuItem.addActionListener(event -> loadAction());

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        saveMenuItem.addActionListener(event -> saveAction());

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setName("MenuExit");
        exitMenuItem.addActionListener(event -> System.exit(0));

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);

        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

    }

    private static void loadAction() {
        try {
            String openedFile = filename.getText().trim();
            String textToRead = Files.readString(Paths.get("/home/muy/IdeaProjects/Text Editor/Text Editor/task/src/" + openedFile));
            filename.setText(openedFile);

            textArea.setText(textToRead);
        } catch (IOException ex) {
            textArea.setText("");
            System.out.printf("An exception occurred %s", ex.getMessage());
        }
    }

    private static void saveAction() {
        String textAreaText = textArea.getText();
        String openedFile = filename.getText().trim();
        File file = new File("/home/muy/IdeaProjects/Text Editor/Text Editor/task/src/" + openedFile);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(textAreaText);
        } catch (IOException ex) {
            System.out.printf("An exception occurred %s", ex.getMessage());
        }
    }

    public static void setMargin(JComponent aComponent, int aTop,
                                 int aRight, int aBottom, int aLeft) {

        Border border = aComponent.getBorder();

        Border marginBorder = new EmptyBorder(new Insets(aTop, aLeft,
                aBottom, aRight));//from   w ww. j  a va2s.  c  o m
        aComponent.setBorder(border == null ? marginBorder
                : new CompoundBorder(marginBorder, border));
    }

}