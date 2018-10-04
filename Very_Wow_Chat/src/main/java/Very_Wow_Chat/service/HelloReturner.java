package Very_Wow_Chat.service;

public class HelloReturner {
	
	    private final long id;
	    private final String content;

	    public HelloReturner(long id, String content) {
	        this.id = id;
	        this.content = content;
	    }

	    public long getId() {
	        return id;
	    }

	    public String getContent() {
	        return content;
	    }

}
