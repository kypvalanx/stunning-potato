package org.gary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.File;
import java.io.IOException;

public class Config {
	private final String token;

	public Config(){
		token = null;
	}

	public Config(String token){
		this.token = token;
	}

	static Config getConfiguration() {
		String token;

		File configFile = new File("resources/config/token.yaml");

		if(configFile.canRead()){
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

			try {
				token = mapper.readValue(configFile, Config.class).getToken();
				return new Config(token);
			} catch (IOException e) {
				e.printStackTrace();
				throw new IllegalStateException("the existing config file is malformed");
			}
		} else
		{
			System.out.println("you don't have a config file i'll add one for you to populate");

			ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
			try {
				new File("resources/config/").mkdir();
				configFile.createNewFile();
				mapper.writeValue(configFile, new Config());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getToken() {
		return token;
	}
}
