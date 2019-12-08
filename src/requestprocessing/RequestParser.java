package requestprocessing;

// MONA'S JOB

// Modul parsare cerere
// modulul asta trebuie sa parcurga O cerere aparuta pe socket
// si sa extraga din ea metoda (GET/POST), resursa ceruta (aici, variabila
// 'filename') si headerele suportate de server (in cerinta zice de 'Accept',
// 'Accept-Charset', 'Authorization', 'Content-type' si 'Host')

import config.Configuration;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

public class RequestParser implements Callable<Void> {
    // TODO: LIMITARE NUMAR MAXIM THREADS

    private Socket socket;
    private HttpResponseCustom httpResponse = new HttpResponseCustom();
    private Configuration configuration;

    public RequestParser(Socket s, Configuration configuration) {
        this.socket=s;
        this.configuration = configuration;
    }

    public Void call() throws IOException { // TODO: in loc de void ar trebui sa fie un obiect de tip HTTPResponse
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

                httpResponse.setOut(out);
                httpResponse.setProtocol("HTTP/1.0"); //protocol de mai sus sau default???

                // TODO: verificare daca metoda e "GET" sau "POST"
                if (!"GET".equals(method))    // ???
                    throw new FileNotFoundException();  // IZA'S JOB --> va fi tratat de o metoda din  clasa Logger

                if (filename.equals("/"))
                    filename="/index.html";
                while (filename.indexOf("/")==0)
                    filename=filename.substring(1);
                filename = filename.replace('/', '\\');

                // URL-ul sa nu contina secventele de caractere "..", ":" sau "|"
                if (filename.contains("..") || filename.contains(":") || filename.contains("|")) {
                    // TODO: exceptie de-asta sau de alt tip?
                    //throw new FileNotFoundException(); // IZA'S JOB --> va fi tratat de o metoda din clasa Logger
                    httpResponse.setContentType("text/html");
                    httpResponse.setStatus(HttpStatus.BAD_REQUEST_400);
                    httpResponse.writeToClient(
                            "<html><head></head><body>"+
                            "Bad Request"+
                            "</body></html>\n"); //TODO de testat mesajul asta de eroare
                    input.close();
                    return null;
                }

                // A resource must have an extension
                // otherwise, it's a directory

                if (new File(filename).isDirectory()) {
                    filename=filename.replace('\\', '/');

                    // --> trebuie apelata o metoda din clasa Logger
                    httpResponse.setContentType("text/html");
                    httpResponse.setStatus(HttpStatus.MOVED_PERMANENTLY_301);
                    httpResponse.writeToClient(
                            "<html><head></head><body>"+
                            "Location: /"+filename+"/\r\n\r\n"+
                            "</body></html>\n"); //TODO de testat mesajul asta de eroare
                    input.close();
                    return null;
                }

                // Open the file (may throw FileNotFoundException for some file types)
                // IZA'S JOB
                // --> va fi tratat de o metoda din  clasa Logger daca arunca
                // exceptie (TODO: vezi documentatia ca sa afli ce tip de exceptie poate arunca functia asta)

                //nu vor fi in root resursele
                filename = configuration.getRelativePath() + filename;

                httpResponse.setContentLength(new File(filename).length());
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

                httpResponse.setStatus(HttpStatus.OK_200);
                httpResponse.setContentType(mimeType);
                httpResponse.setFileInputStream(f);

                httpResponse.writeToClient();
                input.close();
            }
            catch (FileNotFoundException x) {
                httpResponse.setStatus(HttpStatus.NOT_FOUND_404);
                httpResponse.setContentType("text/html");
                httpResponse.writeToClient("<html><head></head><body>"+filename+" not found</body></html>\n");
                input.close();
            }
        }

        catch (IOException excetion) {
            // IZA'S JOB
            // --> se va apela o metoda a clasei Logger
            excetion.printStackTrace(System.out);
            //server error?
            httpResponse.setStatus(HttpStatus.SERVER_ERROR_500); //TODO de testat ca se afiseaza bine
            httpResponse.setContentType("text/html");
            httpResponse.writeToClient("<html><head></head><body>Server error</body></html>\n");
        }

        return null;
    }
}