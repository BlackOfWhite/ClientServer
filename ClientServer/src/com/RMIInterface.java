package com;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {

	/**
	 * CallbackClientIntf interface contains following methods IsGoodObject(Obj,
	 * Obj): compare 2 objects, IsRightValue(long, long): compare 2 longs,
	 */
	String getServerStatus() throws RemoteException;
	boolean validateUser(String name, String password) throws RemoteException;
//	void postToAll(int[] tab) throws RemoteException;
	void bindNumbers(User user, int[] tab) throws RemoteException;
//	void addClient(Client client) throws RemoteException;
	void logError(RemoteException e) throws RemoteException;
	String getWinnersLog() throws RemoteException;
	String checkIfUserWon(User user) throws RemoteException;
	int[] getLastNumbers(User user) throws RemoteException;
	
	//OBERSERVER
//	void update(Object observable, Object updateMsg) throws RemoteException;

}
