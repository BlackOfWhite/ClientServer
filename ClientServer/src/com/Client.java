package com;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * The client program obtains a stub for the registry on the server's host,
 * looks up the remote object's stub by name in the registry, and then invokes
 * the sayHello method on the remote object using the stub.
 * 
 * @author niewinskip
 *
 */
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField passwordTF, inputTF1, inputTF2, inputTF3, nameTF;
	private JTextArea textArea;

	private User user;
	private boolean isLoggedIn;

	private static RMIInterface stub;

	private JPanel top, bottom, bottomLeft, bottomRight, center;

	public Client(String host) {
		this.isLoggedIn = false;
		connect(host);
		makeGUI();
	}

	private void connect(String host) {
		Registry registry;
		String response;
		try {
			registry = LocateRegistry.getRegistry(host);
//			Server s = new Server();
//			Server stub =
//		            (Server) UnicastRemoteObject.exportObject(s, 0);
			stub = (RMIInterface) registry.lookup("RMIInterface");
			response = stub.getServerStatus();
			System.out.println("response: " + response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void makeGUI() {
		frame = new JFrame("Chatroulette");
		JPanel main = new JPanel();
		if (!this.isLoggedIn) {
			JPanel cen = new JPanel();
			passwordTF = new JTextField();
			nameTF = new JTextField();
			JButton bt = new JButton("Ok");
			main.setLayout(new BorderLayout(5, 5));
			cen.setLayout(new GridLayout(3, 0, 5, 5));
			cen.add(new JLabel("Login: "));
			cen.add(nameTF);
			cen.add(new JLabel("Password: "));
			cen.add(passwordTF);
			main.add(cen, BorderLayout.NORTH);
			main.add(bt, BorderLayout.CENTER);
			main.setBorder(new EmptyBorder(10, 10, 10, 10));
			// Events
			bt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						boolean exists = stub.validateUser(nameTF.getText(), passwordTF.getText());
						if (exists) {
							logIn(nameTF.getText(), passwordTF.getText());
//							registerClient();
							frame.dispose();
							makeGUI();
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
						try {
							stub.logError(e1);
						} catch (RemoteException e2) {
							e2.printStackTrace();
						}
					}
				}
			});
			frame.setContentPane(main);
			frame.setSize(300, 150);
			frame.setVisible(true);
		} else {
			top = new JPanel();
			center = new JPanel();
			bottom = new JPanel();
			bottomLeft = new JPanel();
			bottomRight = new JPanel();
			inputTF1 = new JTextField();
			inputTF2 = new JTextField();
			inputTF3 = new JTextField();
			textArea = new JTextArea();
			textArea.setEditable(false);
			JButton btSend = new JButton("Send");
			JButton btRandom = new JButton("Random");
			JButton btWinners = new JButton("Winners");
			JButton btCheckMe = new JButton("Check me");
			JButton btLastNumbers = new JButton("Last numbers");
			// lst = new JList();
			main.setLayout(new BorderLayout(5, 5));
			top.setLayout(new GridLayout(1, 0, 5, 5));
			center.setLayout(new BorderLayout(5, 5));
			bottom.setLayout(new GridLayout(1, 2));
			bottomLeft.setLayout(new GridLayout(1, 3));
			bottomRight.setLayout(new GridLayout(1, 4));
			top.add(new JLabel("Welcome " + user.getName()));
			center.add(new JScrollPane(textArea), BorderLayout.CENTER);
			// cn.add(lst, BorderLayout.EAST);
			bottomLeft.add(inputTF1, BorderLayout.WEST);
			bottomLeft.add(inputTF2, BorderLayout.CENTER);
			bottomLeft.add(inputTF3, BorderLayout.EAST);
			bottomRight.add(btSend, BorderLayout.WEST);
			bottomRight.add(btRandom, BorderLayout.CENTER);
			bottomRight.add(btWinners, BorderLayout.EAST);
			bottomRight.add(btCheckMe, BorderLayout.EAST);
			bottomRight.add(btLastNumbers, BorderLayout.EAST);
			bottom.add(bottomLeft);
			bottom.add(bottomRight);
			main.add(top, BorderLayout.NORTH);
			main.add(center, BorderLayout.CENTER);
			main.add(bottom, BorderLayout.SOUTH);
			main.setBorder(new EmptyBorder(10, 10, 10, 10));
			// Events
			// connect.addActionListener(new ActionListener(){
			// public void actionPerformed(ActionEvent e){ doConnect(); } });
			btSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (inputTF1.getText().isEmpty() || inputTF2.getText().isEmpty() || inputTF3.getText().isEmpty()) {
						updateTextArea("Please fill all fields before you continue");
					} else {
					int[] tab = new int[3];
					tab[0] = Integer.parseInt(inputTF1.getText());
					tab[1] = Integer.parseInt(inputTF2.getText());
					tab[2] = Integer.parseInt(inputTF3.getText());
					sendNumbersToServer(tab);
					updateTextArea("Your current numbers are: " + tab[0] + " " + tab[1] + " " + tab[2]);
					}
				}
			});
			btRandom.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int nr1, nr2, nr3;
						Random gen = new Random();
						nr1 = gen.nextInt(20) + 1;
						nr2 = nr1;
						while (nr2 == nr1) {
							nr2 = gen.nextInt(20) + 1;
						}
						nr3 = nr1;
						while (nr3 == nr1 || nr3 == nr2) {
							nr3 = gen.nextInt(20) + 1;
						}
						inputTF1.setText(Integer.toString(nr1));
						inputTF2.setText(Integer.toString(nr2));
						inputTF3.setText(Integer.toString(nr3));
						
				}
			});
			btWinners.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String msg = "";
					try {
						msg = stub.getWinnersLog();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (msg == null || msg.isEmpty()) {
						updateTextArea("No winners yet");
					} else {
						updateTextArea(msg);
					}
				}
			});
			btCheckMe.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String msg ="";
					try {
						msg = stub.checkIfUserWon(user);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					if (msg == null || msg.isEmpty()) {
						updateTextArea("You did not match any numbers in the last game");
					} else {
						updateTextArea("You won!\n" + msg);
					}
				}
			});
			btLastNumbers.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] tab = null;
					try {
						tab = stub.getLastNumbers(user);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (tab[0] == -1) {
						updateTextArea("There was no lottery yet!");
					} else {
						updateTextArea("Last numbers were: " + tab[0] + " " + tab[1] + " " + tab[2]);
					}
				}
			});
			// tf.addActionListener(new ActionListener(){
			// public void actionPerformed(ActionEvent e){ sendText(); } });
			frame.setContentPane(main);
			frame.setSize(900, 600);
			frame.setVisible(true);
			updateTextArea("Welcome " + user.getName());
		}
	}

	public static void main(String[] args) {
		String host = (args.length < 1) ? null : args[0];
		System.out.println(host);
		new Client(host);
	}

	private void sendNumbersToServer(int[] tab) {
		try {
			stub.bindNumbers(user, tab);
		} catch (RemoteException e) {
			e.printStackTrace();
			try {
				stub.logError(e);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void logIn(String login, String password) {
		User user = new User(login, password);
		setUser(user);
		this.isLoggedIn = true;
	}

	public void logOut() {
		this.isLoggedIn = false;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User u) {
		this.user = u;
	}

	public boolean isLoggedId() {
		return this.isLoggedIn;
	}

//	private void registerClient() {
//		try {
//			stub.addClient(this);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//	}

	public JTextArea getTextArea() {
		return textArea;
	}
	
	public void updateTextArea(String text) {
		String cText = getTextArea().getText();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
//		System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
		getTextArea().setText(cText + "\n" + dateFormat.format(date) + " >> " + text);	
	}
}