package GUI;

import API.APIManager;

public class A {
	static Frontend f;
	static B b;
	static APIManager api = new APIManager();
	public static void main(String[] args) {
		f = new Frontend(api);
		b = new B(api);
		api.attachBackend(b);
		api.attachFrontend(f);
		f.testStart();

	}

}
