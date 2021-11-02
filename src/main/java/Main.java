
import com.jcabi.http.Response;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.JsonResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Main {

    public static List<Repository> repositoryList = new ArrayList<Repository>();
    public static List<User> userList = new ArrayList<User>();

    public static void main(String args[]) {
        String organization;
        int num_of_repos;
        int num_of_contributers;

        // Checking if the commandline parameters' amount true
        if (args.length < 3) {
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

            Response org_response = new JdkRequest("https://api.github.com/orgs/" + organization)
                    .fetch();

            // if organization returns server error, i check it.
            if (org_response.status() != 200) {
                System.out.println("An error occured");
                System.out.println("Response for organization status code : " + org_response.status());
                System.out.println("Terminating");
                System.exit(-1);
            }

            repos_url = org_response.as(JsonResponse.class).json().readObject().getString("repos_url");
            int total_org_repo = org_response.as(JsonResponse.class).json().readObject().getInt("public_repos");


            System.out.println("Fetching Repositories...");
            fill_repo_list(repos_url, total_org_repo);
            System.out.println("Repositories fetched. Total Public repositories : " + repositoryList.size());

            System.out.println("Repositories are being sorted...");
            repositoryList.sort(Comparator.comparing(Repository::getFork_number).reversed());
            System.out.println("Repositories are sorted.");

            // Repositories recording
            System.out.println("Repositories are being written into file...");
            FileWriter repo_fw = new FileWriter(organization + "_repos.csv");
            BufferedWriter repo_bw = new BufferedWriter(repo_fw);
            PrintWriter repo_prw = new PrintWriter(repo_bw);

            // this varible helps in case of number of top repos are greater that organization's total public repos
            int printlen = repositoryList.size() < num_of_repos ? repositoryList.size() : num_of_repos;
            repo_prw.write("repo,forks,url,description\n");

            for (int line = 0; line < printlen; line++) {
                repo_prw.write(repositoryList.get(line).toString());
                repo_prw.flush();
            }
            System.out.println("Repositories written into file.");
            repo_prw.close();

            // users fetched
            System.out.println("Fetching Top users of most " + printlen + " repositories...");
            fill_user_list(num_of_repos, num_of_contributers);
            System.out.println("Users fetched.");

            // users recording
            System.out.println("Users are being written into file...");
            FileWriter user_fw = new FileWriter(organization + "_users.csv");
            BufferedWriter user_bw = new BufferedWriter(user_fw);
            PrintWriter user_prw = new PrintWriter(user_bw);


            user_prw.write("repo,username,contributions,followers\n");

            for (int line = 0; line < userList.size(); line++) {
                user_prw.write(userList.get(line).toString());
                user_prw.flush();
            }


            System.out.println("Users written into file.");
            user_prw.close();

        } catch (IOException ioe) {
            System.out.println("IO Exception:- " + ioe.getMessage());
        }

        System.out.println("Works Done!");
        System.out.println("Exiting...");

    }

    /**
     * This function gets all the public repositories that organization has.
     * In documantaion github api asks for per_page parameter which helps for pagination
     * and in our situation it returns PER_PAGE amount of repository in a single request. So we should increase number of pages.
     * Github sets this value 30 by default and i set it to 100 (max)
     *
     * @param repo_url
     * @param total_repo_count
     */
    public static void fill_repo_list(String repo_url, int total_repo_count) {

        int page_count = (int) Math.ceil(total_repo_count * 1.0 / 100);
        try {
            for (int i = 0; i < page_count; i++) {
                //System.out.println(repo_url+"?per_page=100&page="+(i+1));
                Response repo_response = new JdkRequest(repo_url + "?per_page=100&page=" + (i + 1))
                        .fetch();

                if (repo_response.status() != 200) {
                    System.out.println("An error occurred");
                    System.out.println("Response for repositories status code : " + repo_response.status());
                    System.out.println("Terminating");
                    System.exit(-1);
                }

                // if that page has less repositories that PER_PAGE number, the variable will have the count and prevent nullPointException
                int temp_response_size = repo_response.as(JsonResponse.class).json().readArray().size();
                for (int j = 0; j < temp_response_size; j++) {
                    Repository temp = new Repository();
                    temp.setApi_url(repo_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getString("url"));
                    temp.setRepo_name(repo_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getString("name"));
                    temp.setFork_number(repo_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getInt("forks_count"));
                    temp.setRepo_url(repo_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getString("html_url"));
                    temp.setDescription(repo_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getString("description"));

                    repositoryList.add(temp);
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e.getMessage());
        }
    }

    /**
     * This function takes the parameters that given below, and getting the desired amount of users from the
     * top repositories that has been sorted according to their fork counts.
     *
     * @param top_contributors numbers of top contributors that has been given from command line
     * @param top_repos        numbers of top repositories that has been given from command line
     */
    public static void fill_user_list(int top_contributors, int top_repos) {

        try {
            for (int i = 0; i < top_repos; i++) {
                String contribute_url = repositoryList.get(i).getApi_url() + "/contributors";
                Response contr_response = new JdkRequest(contribute_url)
                        .fetch();

                if (contr_response.status() != 200) {
                    System.out.println("An error occured");
                    System.out.println("Response for contributors status code : " + contr_response.status());
                    System.out.println("Terminating");
                    System.exit(-1);
                }

                int users_cnt = top_contributors < contr_response.as(JsonResponse.class).json().readArray().size() ? top_contributors : contr_response.as(JsonResponse.class).json().readArray().size();
                for (int j = 0; j < users_cnt; j++) {
                    User temp = new User();
                    temp.setRepo_name(repositoryList.get(i).getRepo_name());
                    temp.setCommit_count(contr_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getInt("contributions"));
                    temp.setUsername(contr_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getString("login"));
                    temp.setFollowers(new JdkRequest(contr_response.as(JsonResponse.class).json().readArray().getJsonObject(j).getString("url")).fetch().as(JsonResponse.class).json().readObject().getInt("followers"));
                    System.out.println(temp.toString());
                    userList.add(temp);
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e.getMessage());
        }

    }
}
