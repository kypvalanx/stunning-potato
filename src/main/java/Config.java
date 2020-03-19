public class Config {
	private final String token;

	public Config(){
		token = null;
	}

	public Config(String token){
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
