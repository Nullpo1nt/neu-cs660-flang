package martin.bryant.csu660;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

public class FLANGulator extends JFrame {
    FLANGParser flangParser = FLANGParser.getInstance();
    FLANG       flang       = null;

    JTextArea editor = new JTextArea("{with {x 10}\n\t{with {foo {fun {x} {* x x}}}\n\t\t{+ {call foo x} {call foo {- 11 x}}}}}");   
    JTextArea sysOut = new JTextArea();

    public FLANGulator() {
        try {
            initWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void initWindow() throws Exception {
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        addWindowListener(l);
        
        //Content Pane...
        JPanel contentPane = (JPanel)this.getContentPane();
        this.setSize(400,300);
        this.setTitle("FLANGulator");
        sysOut.setEditable(false);      
        contentPane.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(editor),
                new JScrollPane(sysOut)));
        
        // Menu Items...
        JMenuItem clear = new JMenuItem("Clear",KeyEvent.VK_C);
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editor.setText("");
                sysOut.setText("");
            }
        });
        JMenuItem parse = new JMenuItem("Parse",KeyEvent.VK_P);
        parse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parse();
            }
        });
        JMenuItem run   = new JMenuItem("Run",KeyEvent.VK_R);
        run.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                run();
            }
        });
        JMenuItem exit = new JMenuItem("Exit",KeyEvent.VK_X);
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        JMenu flang = new JMenu("FLANG");
        flang.setMnemonic('F');
        flang.add(clear);
        flang.add(parse);
        flang.add(run);
        flang.addSeparator();
        flang.add(exit);
                
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(flang);
        this.setJMenuBar(menuBar);

        validate();
        setVisible(true);
    }
    
    public boolean parse() {
        String text = editor.getText();
        text = text.trim();
        try {
            flang = flangParser.parse(text);
            sysOut.append("\nParse Completed:\n"+flang.toString());
        } catch (Exception ex) {
            sysOut.append("\n-------\nSyntax Error:\n"+ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public boolean run() {
        if(parse()) {
            
            try {
                String result = (flang.eval()).toString();
                sysOut.append("\nRun Completed,\nResult: "+result);
            } catch (Exception ex) {
                sysOut.append("\n-------\nRun Time Error:\n"+ex.getMessage());
                ex.printStackTrace();
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            setDefaultLookAndFeelDecorated(true);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
           public void run() {
               new FLANGulator();
           }
        });
    }
}
