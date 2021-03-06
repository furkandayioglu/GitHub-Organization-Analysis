/**
 *  User model that we need in order to store spesific data fields of json data
 */
public class User {


    private String repo_name; // passed by repo itself
    private String username;  // json field - login
    private Integer commit_count; // json field - contributions
    private Integer followers ;   // json field - url - followers

    public User() {
        this.repo_name = "";
        this.username = "";
        this.commit_count = 0;
        this.followers = 0;
    }

    /**
     *
     * @param user_url in order to find count of followers
     * @param repo_name the repo that user contributed
     * @param username user's username
     * @param commit_count user's contribution count to the repo that given above
     * @param followers follower count of user
     */
    public User(String user_url, String repo_name, String username, Integer commit_count, Integer followers) {

        this.repo_name = repo_name;
        this.username = username;
        this.commit_count = commit_count;
        this.followers = followers;
    }


    /* Typical Getter Setter methods */
    public String getRepo_name() {
        return repo_name;
    }

    public void setRepo_name(String repo_name) {
        this.repo_name = repo_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCommit_count() {
        return commit_count;
    }

    public void setCommit_count(Integer commit_count) {
        this.commit_count = commit_count;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    /* toString method to print user in csv format */
    @Override
    public String toString() {
        return getRepo_name()+","+getUsername()+","+getCommit_count()+","+getFollowers()+"\n";
    }


}
