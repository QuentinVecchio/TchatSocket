package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//import oracle.jdeveloper.layout.VerticalFlowLayout;

public class ServerCreatView extends JFrame implements ActionListener {
    private ServerSocketController servercont;
    private JLabel jLabel1 = new JLabel();
    private JTextField jTextField1 = new JTextField();
    private JButton jButton1 = new JButton();

    public ServerCreatView(ServerSocketController aServercont ) {
        servercont=aServercont;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
    	this.setSize(400, 100);
        this.setVisible(false);
        this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
        this.setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		this.add(jLabel1, gbc);
		gbc.gridx = 1;
		this.add(jTextField1, gbc);
		gbc.gridx = 2;
		this.add(jButton1, gbc);
        jLabel1.setText("Insérez le numéro du port : ");
        jLabel1.setPreferredSize(new Dimension(180, 30));
        jTextField1.setPreferredSize(new Dimension(120, 30));
        jButton1.setPreferredSize(new Dimension(90, 30));
        jButton1.setText("Lancer");
        jButton1.addActionListener(this);
    }

    public void ErrorPort() {
		JOptionPane.showMessageDialog(null, "Error Port", "Error : Port is a number.", JOptionPane.ERROR_MESSAGE);
	}

	public void Exit() {
		this.setVisible(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jButton1) {
			try {
	            servercont.InitServeur(Integer.parseInt(jTextField1.getText()));
	            servercont.run();
	        }
	        catch(NumberFormatException nfe)
	        {
	        	ErrorPort();
	        }
                        
		}
	}
}
