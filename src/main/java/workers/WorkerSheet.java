package workers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkerSheet {

    /** Application name. */
    private static final String APPLICATION_NAME =
            //"Google Sheets API Java Quickstart";
            "Test application";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

    private static Credential credential = null;
    private static Sheets service = null;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        if (credential == null) {
            InputStream in =
                    Sheet.class.getResourceAsStream("/client_secret.json");
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(
                            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                            .setDataStoreFactory(DATA_STORE_FACTORY)
                            .setAccessType("offline")
                            .build();
            credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver()).authorize("user");
            System.out.println(
                    "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        }
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        if (service == null){
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return service;
    }

    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        Sheets service = getSheetsService();

        // Prints the names and majors of students in a sample spreadsheet:
        // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
        //String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        String spreadsheetId = new Properties().load("/sheets.properties").getProperty("table");
                //"1HyvGs8KKoHfshAlAc_BVH6vIQdBheqtGeu0vDrCjFwo";
                //"17B-dK9kHpdWW1X-0QxQkPH9VYG_1RGs6V2_XDZyzXhc";
//        String range = "Class Data!A2:E";
//        ValueRange response = service.spreadsheets().values()
//                .get(spreadsheetId, range)
//                .execute();
//        List<List<Object>> values = response.getValues();
//        if (values == null || values.size() == 0) {
//            System.out.println("No data found.");
//        } else {
//            System.out.println("Name, Major");
//            for (List row : values) {
//                // Print columns A and E, which correspond to indices 0 and 4.
//                System.out.printf("%s, %s\n", row.get(0), row.get(4));
//            }
//        }
        getPages(spreadsheetId);
        //System.out.printf("%s%n",getPages(spreadsheetId));
    }

    public static Object[][] getValues(String spreadsheetId) throws IOException{
        return getValues(spreadsheetId, "ЖКС2", "A2:B");
    }

    public static Object[][] getValues(String spreadsheetId, String page, String rangePage) throws IOException{
        Sheets service = getSheetsService();

        String range = page + "!" + rangePage;
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        return getRange(values);
    }

    private static Object[][] getRange(List<List<Object>> values) {
        Object[][] result;
        if (values == null || values.size() == 0) {
            result = null;
        }
        else {
            result = new Object[values.size()][2];
            int i = 0;
            for (List row : values) {
                for (int j = 0; j < row.size(); j++) {
                    result[i][j] = row.get(j);

                }
                i++;
            }
        }
        return result;
    }

    public static String[] getHeads(String spreadsheetId, String page) throws IOException {
        Object[] pretendent = getValues(spreadsheetId,page, "A1:1")[0] ;
        String[] result     = new String[pretendent.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) pretendent[i];
        }
        return result;
    }

    public static ArrayList<String> getPages(String spreadsheetId) throws IOException {
        Sheets service = getSheetsService();
        //List<String> sheets =
        //System.out.printf("%s%n",service.spreadsheets().get(spreadsheetId).buildHttpRequest().execute().getContent().readAllBytes());
        //byte[] bytes =
        ArrayList<String> strings = new ArrayList<>();

        Spreadsheet spreadsheet = service.spreadsheets().get(spreadsheetId).execute();
        List<Sheet> sheets = spreadsheet.getSheets();
        System.out.printf("%s%n",spreadsheet.getProperties().getTitle());
        for (Sheet sheet: sheets) {
            //System.out.printf("%s%n",sheet.getProperties().getTitle());
            strings.add(sheet.getProperties().getTitle());
        }
       System.out.printf("%n");
        //String[] strings = (String[])sheets.toArray();
        return strings; //strings;
    }

}
