package comparetest.v2;

public class MethodLevelChangeElseRemoved {

	public int getAwesomeMethod() {
		int foo_mod = 10;
		if (foo_mod == 10) {
			foo_mod = 1;
		} 
		return foo_mod;
	}
}