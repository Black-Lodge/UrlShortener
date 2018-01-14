package urlshortener.blacklodge.metrics;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class DiferentUsers {
	private static Set<String> ips;
	
	public DiferentUsers() {
		ips = new HashSet<String>() ;
	}
	public int getNumber() {
		return ips.size();
	}
	public void add(String word) {
		ips.add(word);
	}
}
