package editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextEditor extends JFrame {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 600;
    private static String filename;
    private static JTextField searchField;

    private static JTextArea textArea;
    private static JFileChooser fileChooser;
    private static JCheckBox checkBox = new JCheckBox("use regex");

    private static ArrayList<SearchResult> searchResults;
    private static int caretPosition;
    private static boolean useRegex = false;

    private static boolean isUseRegex() {
        return useRegex;
    }
    private static  void setUseRegex(boolean value) {
        useRegex = value;
        checkBox.setSelected(value);
    }

    public TextEditor() {
        super("TextEditor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        initComponents();
        setVisible(true);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setName("FileChooser");
//        fileChooser.setVisible(false);
        this.add(fileChooser);
    }

    private void initComponents() {
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setBounds(20, 20, 245, 225);
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
        topPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        add(topPanel, BorderLayout.NORTH);

        JButton openButton = new JButton(new ImageIcon("Text Editor/task/src/open.png"));
        openButton.setName("OpenButton");
        openButton.addActionListener(e -> loadAction());
        topPanel.add(openButton);

        JButton saveButton = new JButton(new ImageIcon("Text Editor/task/src/save.png"));
        saveButton.setName("SaveButton");
        saveButton.addActionListener(e -> saveAction());
        topPanel.add(saveButton);

        searchField = new JTextField();
        searchField.setName("SearchField");
        forceSize(searchField, 120, 35);
        topPanel.add(searchField);

        JButton searchButton = new JButton(new ImageIcon("Text Editor/task/src/search.png"));
        searchButton.setName("StartSearchButton");
        topPanel.add(searchButton);
        searchButton.addActionListener(e -> {
            try {
                searchAction();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        });


        JButton previousButton = new JButton(new ImageIcon("Text Editor/task/src/left.png"));
        previousButton.setName("PreviousMatchButton");
        previousButton.addActionListener(e -> previousAction());
        topPanel.add(previousButton);

        JButton nextButton = new JButton(new ImageIcon("Text Editor/task/src/right.png"));
        nextButton.setName("NextMatchButton");
        nextButton.addActionListener(e -> nextAction());
        topPanel.add(nextButton);

        checkBox.setName("UseRegExCheckbox");
        checkBox.addActionListener(event -> toggleRegexBox());
        topPanel.add(checkBox);


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenuItem loadMenuItem = new JMenuItem("Open");
        loadMenuItem.setName("MenuOpen");
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

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setName("MenuSearch");
        searchMenu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(searchMenu);

        JMenuItem startSearchMenuItem = new JMenuItem("Start Search");
        startSearchMenuItem.setName("MenuStartSearch");
        startSearchMenuItem.addActionListener(event -> {
            try {
                searchAction();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });

        JMenuItem previousMatchMenuItem = new JMenuItem("Previous Match");
        previousMatchMenuItem.setName("MenuPreviousMatch");
        previousMatchMenuItem.addActionListener(event -> previousAction());

        JMenuItem nextMatchMenuItem = new JMenuItem("Next Match");
        nextMatchMenuItem.setName("MenuNextMatch");
        nextMatchMenuItem.addActionListener(event -> nextAction());

        JMenuItem useRegExpMenuItem = new JMenuItem("Use RegEx");
        useRegExpMenuItem.setName("MenuUseRegExp");
        useRegExpMenuItem.addActionListener(event -> toggleRegexBox());

        searchMenu.add(startSearchMenuItem);
        searchMenu.add(previousMatchMenuItem);
        searchMenu.add(nextMatchMenuItem);
        searchMenu.add(useRegExpMenuItem);
    }

    private static void loadAction() {
        try {

            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileAbsolutePath = selectedFile.getAbsolutePath();
                String textToRead = Files.readString(Paths.get(fileAbsolutePath));
                filename = (fileAbsolutePath);
                System.out.println(fileAbsolutePath);
                textArea.setText(textToRead);
            }
        } catch (IOException ex) {
            textArea.setText("");
            System.out.printf("load exception occurred %s", ex.getMessage());
        }
    }

    private static void saveAction() {
        String textAreaText = textArea.getText();
        String openedFile = filename.trim();
        File file = new File(openedFile);
        // int returnValue = jfc.showSaveDialog(null);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(textAreaText);
        } catch (IOException ex) {
            System.out.printf("save exception occurred %s", ex.getMessage());
        }
    }

    private static void searchAction() throws InterruptedException {
        Thread t3 = new Thread(() -> createResults());
        t3.start();
        t3.join();
    }
    private static void toggleRegexBox(){
        setUseRegex(!isUseRegex());
    }
    private static ArrayList<SearchResult> getRegexResult(){
        ArrayList<SearchResult> searchResult = new ArrayList<>();
        String javaText = textArea.getText();
        String textToFind = searchField.getText().trim();

        Pattern searchPattern = Pattern.compile(textToFind, Pattern.CASE_INSENSITIVE);
        Matcher matcher = searchPattern.matcher(javaText);
        while (matcher.find()) {
            int index = matcher.start();
            searchResult.add(new SearchResult(index, matcher.group()));

        }
        return searchResult;
    }

    private static ArrayList<SearchResult> getNormalResult(){
        ArrayList<SearchResult> searchResult = new ArrayList<>();
        String javaText = textArea.getText();
        String textToFind = searchField.getText().trim();
        int start = -1;
        while (true) {
            start = javaText.indexOf(textToFind, start + 1);
            if (start == -1) {
                break;
            }
            searchResult.add(new SearchResult(start, textToFind));


        }
        return searchResult;
    }
    private static void createResults() {
        searchResults = getSearchResults(isUseRegex());
        if (searchResults.size() > 0) {
            setCurrentResultIndex(0);
        } else caretPosition = -1;
    }
private static ArrayList<SearchResult> getSearchResults(boolean isUseRegex){
    return isUseRegex ? getRegexResult() : getNormalResult();

}

    private static void previousAction() {
        if (caretPosition == -1) return;
        int newIndex = caretPosition - 1;
        if (newIndex < 0) newIndex = searchResults.size() - 1;
        setCurrentResultIndex(newIndex);

    }

    private static void nextAction() {
        if (caretPosition == -1) return;
        int newIndex = caretPosition + 1;
        if (newIndex >= searchResults.size()) newIndex = 0;
        setCurrentResultIndex(newIndex);
    }

    private static void setCurrentResultIndex(int index) {
        caretPosition = index;
        setSelection();
    }
    private static void setSelection() {
        if (caretPosition < 0) return;
        highlightCaret(searchResults.get(caretPosition));
    }

    private static void highlightCaret(SearchResult searchResult) {
        if (searchResult == null) return;
        textArea.setCaretPosition(searchResult.start + searchResult.found.length());
        textArea.select(searchResult.start, searchResult.start + searchResult.found.length());
        textArea.grabFocus();
    }

    private static void setMargin(JComponent aComponent, int aTop,
                                  int aRight, int aBottom, int aLeft) {

        Border border = aComponent.getBorder();

        Border marginBorder = new EmptyBorder(new Insets(aTop, aLeft,
                aBottom, aRight));//from   w ww. j  a va2s.  c  o m
        aComponent.setBorder(border == null ? marginBorder
                : new CompoundBorder(marginBorder, border));
    }

    private static void forceSize(JComponent component, int width, int height) {
        Dimension d = new Dimension(width, height);
        component.setMinimumSize(d);
        component.setMaximumSize(d);
        component.setPreferredSize(d);
    }

}

class SearchResult {
    int start;
    String found;

    SearchResult(int start, String found) {
        this.start = start;
        this.found = found;
    }
}