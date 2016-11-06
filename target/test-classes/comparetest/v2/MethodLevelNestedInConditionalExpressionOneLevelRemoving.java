
public class MethodLevelNestedInConditionalExpressionOneLevelRemoving {

	public int getAwesomeMethod() {
		int foo_mod = 10;
		if (foo_mod == 10) {
			foo_mod = 1;
			if (foo_mod == 1) {
				foo_mod = 15;
			}
		} else {
			foo_mod = 2;
		}
		return foo_mod;
	}
}