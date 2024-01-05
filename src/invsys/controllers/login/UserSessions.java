package invsys.controllers.login;

public final class UserSessions {

	private static UserSessions instanceOfUser;

	private static String userName;

	private UserSessions(String user) {
		this.userName = user;
	}

	public static UserSessions setUserSession(String userName) {
		if (instanceOfUser == null) {
			instanceOfUser = new UserSessions(userName);
		}
		return instanceOfUser;
	}

	public void cleanUserSession() {

		instanceOfUser = null;

	}

	public static String getUser() {
		return userName;
	}

	public static UserSessions getSesion() {
		return instanceOfUser;
	}

}
