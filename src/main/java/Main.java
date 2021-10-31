import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    public static void main(String args[]){
        String organization;
        int num_of_repos;
        int num_of_contributers;

        if(args.length < 3){
            System.out.println("### Usage ####");
            System.out.println("Invalid amount of parameters.");
            System.out.println("Program should take, organization name, number of most forked repos and number of top contributers");
            System.out.println("Terminating");
            System.exit(-1);
        }

        organization = args[0];
        num_of_repos = Integer.parseInt(args[1]);
        num_of_contributers = Integer.parseInt(args[2]);


        try {

            URL url = new URL("https://api.github.com/orgs/"+organization);//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+jwon");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
    }
}
