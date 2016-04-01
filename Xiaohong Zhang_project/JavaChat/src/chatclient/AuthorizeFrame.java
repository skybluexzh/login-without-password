package chatclient;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class AuthorizeFrame extends javax.swing.JDialog {
	private Client client;
	private boolean autho = false;
	private JButton yesButton, noButton;
	// private Icon icon;
	private JLabel label;
	private JPanel jPanel;
	//private String authName;
	private JTextPane textPane;
	private String line;
	private String applyName;
	public AuthorizeFrame(Client cli, String li) {
		// super(parent, modal);
		
		
		
		client = cli;
		line= li;
		String appName = line.split(":")[1].split(",")[0];
		System.out.println("authrizeFrame内部显示的line"+line);
		System.out.println("authorizeFrame内部的appName是:"+appName);
		applyName = appName;
		initComponents();
		// this.chatFrame = (ChatFrame)parent;

	}

	private void initComponents() {
		setSize(600, 600);
		ImageIcon icon = new ImageIcon("E:/YES.jpg");
		// 添加
		int width = 600;
		int height = 400;
		icon.setImage(icon.getImage().getScaledInstance(width, height,
				Image.SCALE_DEFAULT));
		label = new JLabel(icon);
		label.setSize(600, 400);
		
		textPane = new JTextPane();
		textPane.setText(textPane.getText()+line);
		

		jPanel = new JPanel(new GridLayout(1, 2));
		yesButton = new JButton("YES");
		noButton = new JButton("NO");
		jPanel.add(yesButton);
		jPanel.add(noButton);

		this.add(label, "North");
		this.add(textPane,"Center");
		this.add(jPanel, "South");

		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				yesButtonAction(evt);
			}
		});

		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				noButtonAction(evt);
			}
		});
	}

	private void yesButtonAction(ActionEvent evt) {
		// TODO Auto-generated method stub
		autho = true;
		client.write("agreement:"+client.getUsername()+":"+applyName);
		dispose();
	}

	private void noButtonAction(ActionEvent evt) {
		// TODO Auto-generated method stub
		autho = false;
		client.write("disagreement:"+client.getUsername()+":"+applyName);
		dispose();
	}
	
}
