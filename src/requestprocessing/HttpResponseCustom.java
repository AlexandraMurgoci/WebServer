package requestprocessing;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

public class HttpResponseCustom {
    private String server; //??
    private LocalDateTime date;
    private LocalDateTime lastModified; //??
    private Long contentLength;
    private String contentType;
    private String protocol;
    private String status;
    private InputStream f;
    private PrintStream out;

    public HttpResponseCustom() {
        date = LocalDateTime.now();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getFileInputStream() {
        return f;
    }

    public void setFileInputStream(InputStream f) {
        this.f = f;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    String getHeader() {
        return protocol + " " + status + "\r\n" +
                "Content-type: " + contentType + "\r\n\r\n";
    }

    void writeToClient() throws IOException {

        out.print(getHeader());

        if(f != null) {
            byte[] a = new byte[4096];
            int n;
            while ((n = f.read(a)) > 0)
                out.write(a, 0, n);
            f.close();
        }
        out.close();
    }

    void writeToClient(String message) throws IOException {

        out.print(getHeader());

        out.print(message);
        out.close();
    }
}
