/**
 * This class is created in order to determine Repository model
 *  We are getting specific datas from json string in order to prevent cast json object into class that has all attributes
 */

public class Repository implements Comparable<Repository> {

    // Class members
    private String repo_name;   // json field - name
    private String api_url;     // json field - url
    private Integer fork_number;    // json filed - forks_count
    private String repo_url;    // json field - html_url
    private String description; // json field - description

    // Constructors

    public Repository() {
        this.repo_name = "";
        this.fork_number = 0;
        this.repo_url = "";
        this.description = "";
    }

    /**
     *
     * @param repo_name repository name that we need
     * @param fork_number how many forks that repository has
     * @param repo_url html url of repository
     * @param description description of repository
     */
    public Repository(String repo_name, Integer fork_number, String repo_url, String description) {
        this.repo_name = repo_name;
        this.fork_number = fork_number;
        this.repo_url = repo_url;
        this.description = description;
    }

    /* Typical Getter Setters */
    public String getRepo_name() {
        return repo_name;
    }

    public void setRepo_name(String repo_name) {
        this.repo_name = repo_name;
    }

    public Integer getFork_number() {
        return fork_number;
    }

    public void setFork_number(Integer fork_number) {
        this.fork_number = fork_number;
    }

    public String getRepo_url() {
        return repo_url;
    }

    public void setRepo_url(String repo_url) {
        this.repo_url = repo_url;
    }

    public String getApi_url() {
        return api_url;
    }

    public void setApi_url(String api_url) {
        this.api_url = api_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* toString method that gives us the object in csv format */
    @Override
    public String toString() {
        return getRepo_name()+","+getFork_number()+","+getRepo_url()+","+getDescription()+"\n";
    }
    // CompareTo method that we need to sort the repository list
    @Override
    public int compareTo(Repository o) {
        if (this.getFork_number() == null || o.getFork_number() == null) {
            return 0;
        }
        return this.getFork_number().compareTo(o.getFork_number());
    }
}
