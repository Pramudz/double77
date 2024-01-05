package invsys.controllers.login;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MainAppLauncher {

	public static void main(String[] args) throws FileNotFoundException {

//		PrintStream out = new PrintStream(new FileOutputStream("resources/errlog.txt"));
//		System.setErr(out);
//
//		PrintStream consoleLog = new PrintStream(new FileOutputStream("resources/consolelog.txt"));
//		System.setOut(consoleLog);

		LoginMain.main(args);

	}

}
