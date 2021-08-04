import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.model.Events.*;


import java.io.*;
import java.security.GeneralSecurityException;
import java.security.PKCS12Attribute;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.*;

public class EventCreation {
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    //private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
   private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = EventCreation.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

   /* private static Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws GeneralSecurityException, IOException {



        //PrivateKey privateKey  = new PKCS12Attribute();
         //       = "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDDw9Xf6NFBAqVH\nlYLLc7meL/iEGD/a2TwEkgRmiUkAcGmQnNpS1XL+ycfwMUZMH5NQnFpVmmRpw4wu\nb3g0T1mSeVqzhvNAhYwtvsX/YcXocxKgxTt2R+4tNbZ1j4vm45+Q+oCrUApnCvsj\nldUEllwez5cj/VCbaR1xdw8HW9L1VHgUFd0dN61Bb3vLwdjjj4oyDjvqr5x32kzH\nUWC8PgkQyyzAI5VGe8ERm7ygqCCPHTVPlmcjelNUS8o6TJSC7WYlRxnceLwFFbfG\nFi+liQnj6h1SJiuMsuynUsng8bV9twApcGpeNpjeMqZyHQOisYd7oHuS5VO5u2Ra\nC2cp355tAgMBAAECggEAVDoc+gJsjcD6parxJGH9Gc9f6E2ao6lVhk3gsNEeZJ74\n/GxQDtWhbXgZmwFIUpY/Rq6Ou0f7LfohhRUWrcL6MegemyFT5Y3P+xqai79gbBue\nhyd05vYZxr/WagxB0CxdSf5Nf+enIiB6XxwnE5sxtVvxll114fe+4EOuZqceQ8i7\ndpo2Kcj6f0sv+cT5acSa4PybqfhVNQdr0OeJyBAyyYOw1qjBaHrotl13T2A3WKVd\nI71diMj8C5GOFdFlDiMLLHb0P4MGGU3K14qVOKPEJu7o3hzbtf8pivUh0A6e4iWA\nyY1SPivyxZxI6tbtJTHUlr37mjEZoUNBQjJJiNYczQKBgQD21qfUO5aD7ufJpCgD\nxfWGQsUdN48dEIJq/LsIgrkI+3avnHbu+LxGQIp4NcJa5fhOIE0g1+WpcfcMRKWI\ni4nO/ZVS5rpX38KJY/EVEqTKVTuuhH+6w6OhVU02IWr1gBMpafQPF/SR49/JKkmq\nofLY4X3c0o6qhNYAPPpdxa63vwKBgQDLB+cyIJBRPXzjtUm1daY/enZDhyOqJOzC\nsWF9Wi1DLfmh3FOOllcyZCLf1T5y89SZ95BendA7eoszbdqW2g3PNpCM/IXzYcPC\nW9AiwtHUM33VuPGIz0etyjiqqWpFDe7p3imjyvkDvZpbRwvUAPsrDTJxEHaVkxdt\n/xeFgkzU0wKBgQDl9ww61R0gY3Z8zLwe79ITuKct1jI18MigZj7V6fv7m1NyvoGr\nl/7IMJ8Q6Xzd9LzGBJ/2xk2H9M4mFtplgSLtviDdMGpRLf1uxLa5XDo86vWEXJsn\nY9dRmkgp8y5T9HNltPmVCktnk/ZM02H4X5MBbes1zNDvmnxJkoXq0R46vwKBgAnx\nxyczZR41jspwdL3Il24Ah45jLiIFUPtwSMAlDJZ/x/xfmFAhmP/E29huz3I98ynf\ncFjSDgL7y5mnRc+huo9tssLVE5KKNVZtvNIXDMQCCaXQOzRiQraGcyQYzDW5qvwV\nf6CgLnY6cTcLUc7+hU7khJX5InXNEJa4qeRGL5E1AoGAMM9uWs9z2l4IQamCnNtW\nPA1QQ44jX9l71JJm0Nk1LiHCr+8oqCNB8Iu50KFT0kYW/UDjYD2X/mmNo7+tZLSa\nfzynp1mczh7GwAX4ZuM+jdYXS1uoaxoGYSdnIIegvkRKVoPV7D3WQ0On7vnykbHA\nuFDlderAgmNwDZvmh79uJ2A=\n-----END PRIVATE KEY-----\n";

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("C:\\Users\\rekhas\\IntellijWorspace\\CalendarDemo\\src\\main\\resources\\serviceacoountcredentials.json"))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR)).createDelegated("rekha.sharma@talentpool.website");
System.out.println(credential.getAccessToken());

        return credential;
    }*/

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        //NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();


        Event event = new Event()
                .setSummary("Google I/O 2015")
                .setLocation("800 Howard St., San Francisco, CA 94103")
                .setDescription("A chance to hear more about Google's developer products.");

        DateTime startDateTime = new DateTime("2021-08-21T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        ConferenceData conferenceData = new ConferenceData();
        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
        ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey();
        conferenceSolutionKey.setType("hangoutsMeet");
        createConferenceRequest.setRequestId("1");
        createConferenceRequest.setConferenceSolutionKey(conferenceSolutionKey);
        conferenceData.setCreateRequest(createConferenceRequest);
        DateTime endDateTime = new DateTime("2021-08-21T17:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));
        event.setConferenceData(conferenceData);

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("rekha.sharma@talentica.com"),
                new EventAttendee().setEmail("satish.kumar@talentica.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        System.out.println(event.getAttendees());
        event = service.events().insert(calendarId, event).setConferenceDataVersion(1).setSendNotifications(true).execute();
       System.out.println("Event created:" + event.getHtmlLink());
    }
}