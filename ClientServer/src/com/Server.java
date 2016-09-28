package com;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server implements RMIInterface {

	// private List<Client> clientsList;
	private static List<User> usersList;
	private static int nr1, nr2, nr3;
	private static String winnersLog;

	// SINGLETON
	private static Server instance = null;

	protected Server() {
		this.usersList = new ArrayList<User>();
		usersList.add(new User("a", "b"));
		usersList.add(new User("b", "c"));
		usersList.add(new User("c", "d"));
		winnersLog = "";
	}
	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}
	//SINGLETON

//	public Server() {
//		// this.usersList = new ArrayList<User>();
//		usersList.add(new User("a", "b"));
//		usersList.add(new User("b", "c"));
//		usersList.add(new User("c", "d"));
//		winnersLog = "";
//	}

	public static void main(String[] args) {
		try {
			Server server = new Server();
			System.setProperty(Constants.POLICY_PROPERTY_NAME, Constants.POLICY_FILE);
			// if (System.getSecurityManager() == null) {
			// System.setSecurityManager(new SecurityManager());
			// }
			/**
			 * Exports the supplied remote object to receive incoming remote
			 * method invocations on an anonymous TCP port and returns the stub
			 * for the remote object to pass to clients. As a result of the
			 * exportObject call, the runtime may begin to listen on a new
			 * server socket or may use a shared server socket to accept
			 * incoming remote calls for the remote object. The returned stub
			 * implements the same set of remote interfaces as the remote
			 * object's class and contains the host name and port over which the
			 * remote object can be contacted.
			 */
			RMIInterface stub = (RMIInterface) UnicastRemoteObject.exportObject(server, 0);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("RMIInterface", stub);
			System.err.println("Server ready");
			startGenerator();
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public String getServerStatus() {
		return "Server is running.";
	}

	@Override
	public boolean validateUser(String name, String password) {
		User u = new User(name, password);
		for (User user : usersList) {
			if (u.getName().equals(user.getName()) && u.getPassword().equals(user.getPassword())) {
				return true;
			}
		}
		return false;
	}

	// @Override
	// public void addClient(Client client) {
	// this.clientsList.add(client);
	// }
	//
	// @Override
	// public void postToAll(int[] tab) {
	// System.err.println("Clients currently running: " + clientsList.size());
	// for (Client client : clientsList) {
	// System.err.println("Sending '" + text + "' to user " +
	// client.getUser().getName());
	// if (client.isLoggedId()) {
	// client.updateTextArea(text);
	// } else {
	// System.err.println(">>User not logged in");
	// }
	// }
	// }

	@Override
	public void bindNumbers(User u, int[] tab) {
		// System.err.println("Clients currently running: " +
		// clientsList.size());
		// for (Client client : clientsList) {
		// System.err.println("Sending '" + text + "' to user " +
		// client.getUser().getName());
		// if (client.isLoggedId()) {
		// client.updateTextArea(text);
		// } else {
		// System.err.println(">>User not logged in");
		// }
		// }
		System.err.println("Users currently registred: " + usersList.size());
		for (User user : usersList) {
			if (user.getName().equals(u.getName()) && user.getPassword().equals(u.getPassword())) {
				user.setNumbers(tab);
				break;
			}
		}
	}

	@Override
	public void logError(RemoteException e) {
		System.out.println(e);
	}

	private static void startGenerator() {
		try {
			while (true) {
				generate3Numbers();
				System.out.println("*** The winning numbers are: " + nr1 + " " + nr2 + " " + nr3 + " ***");
				postWinners();
				clearUserNumbers();
				int seconds = 20;
				Thread.sleep(seconds * 1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void generate3Numbers() {
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
	}

	private static void clearUserNumbers() {
		int[] tab = new int[3];
		tab[0] = -1;
		tab[1] = -1;
		tab[2] = -1;
		for (User user : usersList) {
			user.setNumbers(tab);
		}
	}

	private static void postWinners() {
		List<User> winners1 = new ArrayList<>();
		List<User> winners2 = new ArrayList<>();
		List<User> winners3 = new ArrayList<>();
		for (User user : usersList) {
			int matches = 0;
			int[] t = user.getNumbers();
			for (int x = 0; x <= 2; x++) {
				if (t[x] == nr1 || t[x] == nr2 || t[x] == nr3) {
					matches++;
				}
			}
			if (matches == 3) {
				winners3.add(user);
			} else if (matches == 2) {
				winners2.add(user);
			} else if (matches == 1) {
				winners1.add(user);
			}
		}
		winnersLog = "---Main prize---\n";
		// System.out.println("---Main prize---");
		for (User u : winners3) {
			// System.out.println(u.getName() + " ");
			winnersLog += u.getName() + ",";
		}
		winnersLog += "---Second place---\n";
		for (User u : winners2) {
			winnersLog += u.getName() + ",";
		}
		winnersLog += "---Third place---\n";
		for (User u : winners1) {
			winnersLog += u.getName() + ",";
		}
		System.out.println(winnersLog);
	}

	@Override
	public String getWinnersLog() {
		return winnersLog;
	}

	@Override
	public int[] getLastNumbers(User user) {
		int[] tab = new int[3];
		tab[0] = nr1;
		tab[1] = nr2;
		tab[2] = nr3;
		return tab;
	}

	@Override
	public String checkIfUserWon(User u) {
		for (User user : usersList) {
			if (user.getName().equals(u.getName()) && user.getPassword().equals(u.getPassword())) {
				int matches = 0;
				String msg = "You matched numbers:";
				int[] t = user.getNumbers();
				for (int x = 0; x <= 2; x++) {
					if (t[x] == nr1 || t[x] == nr2 || t[x] == nr3) {
						matches++;
						msg += " " + t[x];
					}
				}
				if (matches > 0) {
					return msg;
				}
			}
		}
		return "";
	}
}
