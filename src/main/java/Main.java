import com.jcabi.http.Response;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.JsonResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.Comparators;
import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.json.*;

public class Main {

    public static List<Repository> repositoryList = new ArrayList<Repository>();

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

            if(org_response.status() != 200 ){
                System.out.println("An error occured");
                System.out.println("Response for organization status code : "+org_response.status());
                System.out.println("Terminating");
                System.exit(-1);
            }

            repos_url = org_response.as(JsonResponse.class).json().readObject().getString("repos_url");
            int total_org_repo = org_response.as(JsonResponse.class).json().readObject().getInt("public_repos");

            //System.out.println(repos_url);
            //System.out.println(total_org_repo);
            System.out.println("Fetching Repositories...");
            fill_repo_list(repos_url,total_org_repo);
            System.out.println("Repositories fetched. Total Public repositories : "+repositoryList.size());

            System.out.println("Repositories are being sorted...");
            repositoryList.sort(Comparator.comparing(Repository::getFork_number).reversed());
            System.out.println("Repositories are sorted.");

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
    }

    public static void fill_repo_list(String repo_url,int total_repo_count){

       int page_count = (int)Math.ceil(total_repo_count*1.0/100);
       try {
           for (int i = 0; i < page_count; i++) {
               //System.out.println(repo_url+"?per_page=100&page="+(i+1));
               Response repo_response = new JdkRequest(repo_url+"?per_page=100&page="+(i+1))
                       .fetch();

               if (repo_response.status() != 200) {
                   System.out.println("An error occured");
                   System.out.println("Response for repositories status code : " + repo_response.status());
                   System.out.println("Terminating");
                   System.exit(-1);
               }
               int temp_response_size = repo_response.as(JsonResponse.class).json().readArray().size();
               for(int j = 0 ; j < temp_response_size;j++){
                   Repository temp = new Repository();
                   temp.setRepo_name(repo_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getString("name"));
                   temp.setFork_number(repo_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getInt("forks_count"));
                   temp.setRepo_url(repo_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getString("html_url"));
                   temp.setDescription(repo_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getString("description"));

                   repositoryList.add(temp);
               }
           }

       }catch (IOException ioe){
           System.out.println("Exception in NetClientGet:- " + ioe.getMessage());
       }
    }
}
