import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.undo.*;

public class TextEditor extends JFrame implements ActionListener {
    JTextArea textArea;
    JScrollPane scrollPane;
    UndoManager undoManager;
    JSpinner fontSizeSpinner;
    JComboBox<String> fontBox;
    JCheckBoxMenuItem wrapToggle;

    JMenuItem openItem, saveItem, exitItem;
    JMenuItem undoItem, redoItem;
    JMenuItem cutItem, copyItem, pasteItem;
    JMenuItem darkModeItem, lightModeItem;

    public TextEditor() {
        setTitle("Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        undoManager = new UndoManager();

        textArea = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); 

                Graphics2D g2 = (Graphics2D) g.create();
                int caretPos = getCaretPosition();
                try {
                    Rectangle r = modelToView(caretPos);
                    if (r != null) {
                        g2.setColor(new Color(200, 200, 200, 80)); 
                        g2.fillRect(0, r.y, getWidth(), r.height);
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

                g2.setColor(new Color(230, 230, 230));
                int lineHeight = getFontMetrics(getFont()).getHeight();
                int lineCount = getHeight() / lineHeight + 1;
                int caretLine = 0;
                try {
                    caretLine = getLineOfOffset(getCaretPosition());
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < lineCount; i++) {
                    if (i != caretLine) {
                        int y = (i + 1) * lineHeight;
                        g2.drawLine(0, y, getWidth(), y);
                    }
                }

                g2.dispose();
            }
        };

        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setMargin(new Insets(5, 5, 5, 5));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setRowHeaderView(new TextLineNumber(textArea));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fontLabel = new JLabel("Font:");

        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(20, 8, 72, 1));
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.addChangeListener(e -> {
            int size = (int) fontSizeSpinner.getValue();
            textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, size));
        }
        );

        JButton fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Choose Text Color", textArea.getForeground());
            if (color != null) textArea.setForeground(color);
        }
        );

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox<>(fonts);
        fontBox.setSelectedItem("Arial");
        fontBox.addActionListener(e -> textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize())));

        topPanel.add(fontLabel);
        topPanel.add(fontSizeSpinner);
        topPanel.add(fontColorButton);
        topPanel.add(fontBox);
        add(topPanel, BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        undoItem = new JMenuItem("Undo");
        redoItem = new JMenuItem("Redo");
        cutItem = new JMenuItem("Cut");
        copyItem = new JMenuItem("Copy");
        pasteItem = new JMenuItem("Paste");
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        undoItem.addActionListener(e -> { if (undoManager.canUndo()) undoManager.undo(); });
        redoItem.addActionListener(e -> { if (undoManager.canRedo()) undoManager.redo(); });
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        menuBar.add(editMenu);

        JMenu viewMenu = new JMenu("View");
        wrapToggle = new JCheckBoxMenuItem("Line Wrap", true);
        wrapToggle.addActionListener(e -> textArea.setLineWrap(wrapToggle.isSelected()));
        viewMenu.add(wrapToggle);
        menuBar.add(viewMenu);

        JMenu modeMenu = new JMenu("Mode");
        darkModeItem = new JMenuItem("Dark Mode");
        lightModeItem = new JMenuItem("Light Mode");
        darkModeItem.addActionListener(e -> setDarkMode());
        lightModeItem.addActionListener(e -> setLightMode());
        modeMenu.add(darkModeItem);
        modeMenu.add(lightModeItem);
        menuBar.add(modeMenu);

        setJMenuBar(menuBar);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void setDarkMode() {
        textArea.setBackground(new Color(30, 30, 30));
        textArea.setForeground(new Color(210, 210, 210));
        textArea.setCaretColor(Color.WHITE);
    }

    private void setLightMode() {
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setCaretColor(Color.BLACK);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    textArea.read(reader, null);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (src == saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.print(textArea.getText());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (src == exitItem) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextEditor::new);
    }
}