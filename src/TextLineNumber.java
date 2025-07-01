import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Element;
import java.awt.*;

public class TextLineNumber extends JPanel implements CaretListener, DocumentListener {
    private static final int MARGIN = 5;
    private JTextArea textArea;

    public TextLineNumber(JTextArea textArea) {
        this.textArea = textArea;
        textArea.addCaretListener(this);
        textArea.getDocument().addDocumentListener(this);
        setPreferredWidth();
    }

    private void setPreferredWidth() {
        int lines = textArea.getLineCount();
        int digits = Math.max(3, String.valueOf(lines).length());
        int width = digits * textArea.getFontMetrics(textArea.getFont()).charWidth('0') + 2 * MARGIN;
        Dimension d = getPreferredSize();
        d.setSize(width, Integer.MAX_VALUE);
        setPreferredSize(d);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
        int fontHeight = fm.getHeight();
        int startOffset = textArea.viewToModel(new Point(0, 0));

        try {
            int startLine = textArea.getLineOfOffset(startOffset);
            int endLine = textArea.getLineCount();
            for (int i = startLine; i < endLine; i++) {
                int y = i * fontHeight + fontHeight - 4;
                String lineNum = String.valueOf(i + 1);
                g.drawString(lineNum, getWidth() - fm.stringWidth(lineNum) - MARGIN, y);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        repaint();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        setPreferredWidth();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        setPreferredWidth();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setPreferredWidth();
    }
}
