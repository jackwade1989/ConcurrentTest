package Concurrent_21_3;

public abstract class Practice_1 {
	private String title;
	
	private String answer;
	
	public abstract void init(String str1,String str2,int id);

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
}
