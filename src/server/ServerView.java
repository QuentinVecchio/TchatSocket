package server;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

//import oracle.jdeveloper.layout.PaneConstraints;
//import oracle.jdeveloper.layout.PaneLayout;

public class ServerView extends JFrame  {

    String Message[] = {"adrien","pierre","michel"};
    String clients[]= {"adrien","pierre","michel"};
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    //private PaneLayout paneLayout1 = new PaneLayout();
    private JPanel jPanel2 = new JPanel();
    private JPanel jPanel3 = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JLabel jLabel1 = new JLabel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel jPanel4 = new JPanel();
    private JList jList1;
    private JList jList2;
    
    public ServerView() {
        super();
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        
        
        jList1= new JList(Message);
        jList2= new JList(clients);
        
        //jPanel1.setLayout(paneLayout1);
        jTabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        jPanel2.setLayout(borderLayout1);
        jPanel3.setLayout(borderLayout2);
        jLabel1.setText("messege en temps directe :");
        jPanel2.add(jList1, BorderLayout.CENTER);
        jPanel2.add(jLabel1, BorderLayout.NORTH);
        jTabbedPane1.addTab("adrien", jPanel2);
        jPanel3.add(jList2, BorderLayout.NORTH);
        jPanel3.add(jPanel4, BorderLayout.CENTER);
        jTabbedPane1.addTab("bonjour", jPanel3);
        /*jPanel1.add(jTabbedPane1,
                    new PaneConstraints("jTabbedPane1", "jTabbedPane1",
                                        PaneConstraints.ROOT, 1.0f));*/
        this.getContentPane().add(jTabbedPane1, null);
        this.setSize(800, 500);
        this.setVisible(true);
    }
}
