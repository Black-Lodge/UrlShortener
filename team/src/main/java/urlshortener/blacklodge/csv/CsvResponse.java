package urlshortener.blacklodge.csv;

public class CsvResponse {
	String from;
	String to;
	boolean correct;
	String cause;
	
	public CsvResponse(String from, String to, boolean correct, String cause) {
		super();
		this.from = from;
		this.to = to;
		this.correct = correct;
		this.cause = cause;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
	
}
