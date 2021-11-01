import com.jcabi.http.Response;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.JsonResponse;
import org.codehaus.jackson.map.ObjectMapper;
import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.*;

public class Main {

    public static void main(String args[]){
        String organization;
        int num_of_repos;
        int num_of_contributers;

        // Checking if the commandline parameters' amount true
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
        String repos_url;

        try {

            Response org_response = new JdkRequest("https://api.github.com/orgs/"+organization)
                                        .fetch();

            if(org_response.status() < 200 || org_response.status()>299){
                System.out.println("An error occured");
                System.out.println("Response status code : "+org_response.status());
                System.out.println("Terminating");
                System.exit(-1);
            }

            repos_url = org_response.as(JsonResponse.class).json().readObject().getString("repos_url");

            System.out.println(repos_url);
        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
    }
}
