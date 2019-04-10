package kiryakova.izot.error;

public class ErrorInfo {
    public final String url;
    public final String e;

    public ErrorInfo(String url, Exception e) {
        this.url = url;
        this.e = e.getLocalizedMessage();
    }

}
