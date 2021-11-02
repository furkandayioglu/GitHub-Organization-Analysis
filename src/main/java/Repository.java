public class Repository {

    // Class members
    private String repo_name;   // json field - name
    private int fork_number;    // json filed - forks_count
    private String repo_url;    // json field - html_url
    private String description; // json field - description

    // Constructors

    public Repository() {
        this.repo_name = "";
        this.fork_number = 0;
        this.repo_url = "";
        this.description = "";
    }

    public Repository(String repo_name, int fork_number, String repo_url, String description) {
        this.repo_name = repo_name;
        this.fork_number = fork_number;
        this.repo_url = repo_url;
        this.description = description;
    }

    public String getRepo_name() {
        return repo_name;
    }

    public void setRepo_name(String repo_name) {
        this.repo_name = repo_name;
    }

    public int getFork_number() {
        return fork_number;
    }

    public void setFork_number(int fork_number) {
        this.fork_number = fork_number;
    }

    public String getRepo_url() {
        return repo_url;
    }

    public void setRepo_url(String repo_url) {
        this.repo_url = repo_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getRepo_name()+";"+getFork_number()+";"+getRepo_url()+";"+getDescription()+"\n";
    }
}
