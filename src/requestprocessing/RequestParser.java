package requestprocessing;

// MONA'S JOB

// Modul parsare cerere
// modulul asta trebuie sa parcurga O cerere aparuta pe socket
// si sa extraga din ea metoda (GET/POST), resursa ceruta (aici, variabila
// 'filename') si headerele suportate de server (in cerinta zice de 'Accept',
// 'Accept-Charset', 'Authorization', 'Content-type' si 'Host')

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

public class RequestParser implements Callable<Void> {
    // TODO: LIMITARE NUMAR MAXIM THREADS

    private Socket socket;

    public RequestParser(Socket s) {
        this.socket=s;
    }

    public Void call() { // TODO: in loc de void ar trebui sa fie un obiect de tip HTTPResponse
        try {

            String method=""; // "GET"/"POST"
            String filename=""; // "imgs/viezure.jpeg"
            String protocol=""; // "HTTP1/0" / "HTTP1/1"
            InputStreamReader ISR= new InputStreamReader(socket.getInputStream());
            BufferedOutputStream BOS=new BufferedOutputStream(socket.getOutputStream());
            BufferedReader input=new BufferedReader(ISR);
            PrintStream out=new PrintStream(BOS);
            String getRequest=input.readLine();
            Thread t = Thread.currentThread();
            System.out.println(getRequest+" Name - "+ t.getName());  // ELEMENT de Logger

            String[] arrOfStr = getRequest.split(" ");
            try {
                method=arrOfStr[0];
                filename=arrOfStr[1];
                protocol=arrOfStr[2];

                // TODO: verificare daca metoda e "GET" sau "POST"
                if (!"GET".equals(method))    // ???
                    throw new FileNotFoundException();  // IZA'S JOB --> va fi tratat de o metoda din  clasa Logger

                if (filename.equals("/"))
                    filename="/index.html";
                while (filename.indexOf("/")==0)
                    filename=filename.substring(1);
                filename = filename.replace('/', '\\');

                // URL-ul sa nu contina secventele de caractere "..", ":" sau "|"
                if (filename.contains("..") || filename.contains(":") || filename.contains("|"))
                    // TODO: exceptie de-asta sau de alt tip?
                    throw new FileNotFoundException(); // IZA'S JOB --> va fi tratat de o metoda din clasa Logger

                // A resource must have an extension
                // otherwise, it's a directory

                if (new File(filename).isDirectory()) {
                    filename=filename.replace('\\', '/');

                    // --> trebuie apelata o metoda din clasa Logger
                    out.print("HTTP/1.0 301 Moved Permanently\r\n"+
                            "Location: /"+filename+"/\r\n\r\n");
                    out.close();
                    return null;
                }

                // Open the file (may throw FileNotFoundException for some file types)
                // IZA'S JOB
                // --> va fi tratat de o metoda din  clasa Logger daca arunca
                // exceptie (TODO: vezi documentatia ca sa afli ce tip de exceptie poate arunca functia asta)
                InputStream f=new FileInputStream(filename);

                // Determine the MIME type and print HTTP header
                String mimeType="text/plain";

                if (filename.endsWith(".html") || filename.endsWith(".htm"))
                    mimeType="text/html";
                else if (filename.endsWith(".php")) mimeType="text/x-php";  // CORLAN'S JOB --> apel la modulul INTERPRETOR PHP
                else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
                    mimeType="image/jpeg";
                else if (filename.endsWith(".pdf"))  // ADDED
                {mimeType="application/pdf";}

                // TODO: CORLAN
                // Content type field must support: application/pdf application/json application/xml
                // image/gif image/jpeg image/png image/tiff text/css text/html text/javascript text/plain text/xml

                // aici eu am gandit sa fie o clasa separata care construieste
                // raspunsul HTTP si care sa o trimita inapoi la Main si ulterior la
                // browser-ul clientului, la modul ca ai scrie
                // HTTPResponse r = new HTTPResponse();
                // r.append("HTTP/1.0 200 OK [...]");
                out.print("HTTP/1.0 200 OK\r\n"+
                        "Content-type: " + mimeType + "\r\n\r\n");

                // Send the resource to client
                // la fel ca mai sus: clasa HTTPResponse trebuie sa faca asta
                byte[] a=new byte[4096];
                int n;
                while ((n=f.read(a))>0)
                    out.write(a, 0, n);
                out.close();
            }
            catch (FileNotFoundException x) {
                // MONA'S JOB
                // la fel ca mai sus: clasa de constructie HTTP response trebuie sa faca asta
                out.println("HTTP/1.0 404 Not Found\r\n"+ // TODO: testeaza cu Burp daca se transmit astea
                        "Content-type: text/html\r\n\r\n"+
                        "<html><head></head><body>"+filename+" not found</body></html>\n");
                out.close();
            }
        }

        catch (IOException errorMessage) {
            // IZA'S JOB
            // --> se va apela o metoda a clasei Logger
            System.out.println(errorMessage);
        }

        // TODO: sa returneze un obiect tip HTTPResponse
        return null;
    }
}