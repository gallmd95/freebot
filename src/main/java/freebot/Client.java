package freebot;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

public class Client {


    Client(){
        final JFrame frame = new JFrame();

        final JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.getCaret().setVisible(true);

        ta.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String text = ta.getText().substring(1);
                    final int last = text.lastIndexOf('\n');
                    if(last > -1) text = text.substring(last+2);
                    doSomething(text);
                    ta.append("\n>");
                } else if (e.getKeyCode() > 64 && e.getKeyCode() < 91){
                    ta.append(String.valueOf(e.getKeyChar()));
                    ta.setText(new StringBuilder(ta.getText()).insert(ta.getCaretPosition(), e.getKeyChar()).toString());
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
    private void doSomething(final String text){
        System.out.println(text);
    }
}
