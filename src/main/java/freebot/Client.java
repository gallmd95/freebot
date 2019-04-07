package freebot;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.Arrays;

public class Client {
    private final CommandFactory commandFactory;

    Client(CommandFactory commandFactory){
        this.commandFactory = commandFactory;

        final JFrame frame = new JFrame();
        final JTextArea ta = new JTextArea();
        ta.setEditable(false);
        Arrays.stream(ta.getKeyListeners()).forEach(ta::removeKeyListener);
        ta.getCaret().setVisible(true);
        Action beep = ta.getActionMap().get(DefaultEditorKit.deletePrevCharAction);
        beep.setEnabled(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ta.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String text = ta.getText().substring(1);
                    final int last = text.lastIndexOf('\n');
                    if(last > -1) text = text.substring(last+2);
                    runCommand(text, ta);
                } else if (e.getKeyCode() > 64 && e.getKeyCode() < 91 || e.getKeyCode()==32 || e.getKeyCode()==8){
                    final int caretPosition = ta.getCaretPosition();
                    final int last = ta.getText().lastIndexOf('\n');
                    final int lastLineStart = last>-1 ? last+2 : 1;
                    final int lastCharForSelection = last>-1 ? last+1 : 0;
                    if(e.getKeyCode()==8 && ta.getSelectedText()!=null && ta.getSelectionStart()>lastCharForSelection){
                        ta.replaceSelection("");
                    } else if (e.getKeyCode()==8 && caretPosition>lastLineStart) {
                        ta.setText(new StringBuilder(ta.getText()).deleteCharAt(caretPosition-1).toString());
                        ta.setCaretPosition(caretPosition-1);
                    } else if (caretPosition>lastLineStart-1) {
                        ta.insert(String.valueOf(e.getKeyChar()), ta.getCaretPosition());
                    }
                }
            }
        });

        TextAreaOutputStream taos = new TextAreaOutputStream( ta, "");
        PrintStream ps = new PrintStream( taos );
        //System.setOut( ps );
        //System.setErr( ps );


        frame.add( new JScrollPane( ta )  );

        frame.pack();
        frame.setVisible( true );
        frame.setSize(600,400);
    }
    private void runCommand(final String text, final JTextArea ta){
        final String[] inputs = text.trim().toUpperCase().split("\\s+");
        commandFactory.makeCommand(inputs[0]).run(inputs, ta);

    }
}
