package comparetest.v1;

public class MethodLevelChangeElseRemoved {

	public int getAwesomeMethod() {
		int foo_mod = 10;
		if (foo_mod == 10) {
			foo_mod = 1;
		} else {
			foo_mod = 2;
		}
		return foo_mod;
	}
}