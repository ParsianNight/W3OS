import java.io.*;
import java.util.Scanner;
import java.util.Stack;
import java.nio.file.Files;

public class Proccess extends w3OS implements Runnable {


	private int PID; // process ID
	private int PPID; // Parent process ID
	private int UID; // User ID
	private int[] User_Visible_Registers;
	private Status status;

	private Priority Priority;
	private Stack<Object> StackPointer;
	private boolean memoryPrivileges;
	int execution_time;//new variable
	private int RemTime; // Omar
	public final Thread t ;
	private char type ;
	private String FileName ;
	private  String data ;

/*
 * Tried to fit as many realistic variables as used in nowadays operating systems.
 * 
 */
	
	public Proccess( int ppid,int pid, Status status,  Priority p, boolean privliged) throws InterruptedException {
		PID = pid;
		PPID = ppid;
		this.status = status;
		Priority = p;
		memoryPrivileges = privliged;
		t = new Thread(this);
	}

	public int getPID() {
		return PID;
	}
	public void setPID(int pID) {
		PID = pID;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	/*
	 * This was used.
	 * 
	 */
	
	public void DecrementRemTime(int RemTime) {
		System.out.println("Process " + PID + "run for " + (this.RemTime - RemTime));
		this.RemTime = Math.max((this.RemTime - RemTime), 0);

	}

	public int getRemTime() {
		return RemTime;
	}

	public Priority getPriority() {
		return Priority;
	}

	public void setPriority(Priority p) {
		this.Priority = p;
	}
	public void ProcessB() throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please Enter The File Name");
		String fileName = sc.next();
		this.FileName =assign("File_name",fileName,this.PID);
		System.out.println("Please Enter The data");
		String data = sc.next();
		this.data = assign("data",data, this.PID);
		type = 'B';
		w3OS.Proccesses.add(this);
	}
	public void ProcessA() throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please Enter The File Name");
		String fileName = sc.next();
		this.FileName =assign("File_name",fileName,this.PID);
		type= 'A';
		w3OS.Proccesses.add(this);
	}
	public void remove(){
		for(int i =0 ; i < pNonprivleged ; i++){
			if(mem[i] instanceof Proccess){
				if(mem[i] == this){
					mem[i]=null;
				}
			}
		}
	}

	@Override
	public void run() {

		synchronized (this) {
			if (type == 'B') {
				w3OS.mem[pNonprivleged++] = this;
				try {
					writeFile(FileName, data, this.PID);
					this.setStatus(Status.Finished);
				} catch (IOException e) {
					e.printStackTrace();
				}
				remove();
			} else {
				System.out.println(this.getPID());
				w3OS.mem[pNonprivleged++] = this;
				try {
					readFile(FileName, this.PID);
					this.setStatus(Status.Finished);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
				remove();
			}
		}
	}

}
