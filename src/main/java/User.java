public class User {

    private String user_url;  // json field - url
    private String repo_name; // passed by repo itself
    private String username;  // json field - login
    private int commit_count; // json field - contributions
    private int followers ;   // json field - url - followers

    public User() {
    }

    public User(String user_url, String repo_name, String username, int commit_count, int followers) {
        this.user_url = user_url;
        this.repo_name = repo_name;
        this.username = username;
        this.commit_count = commit_count;
        this.followers = followers;
    }

    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }

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

    public int getCommit_count() {
        return commit_count;
    }

    public void setCommit_count(int commit_count) {
        this.commit_count = commit_count;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    @Override
    public String toString() {
        return getRepo_name()+";"+getUsername()+";"+getCommit_count()+";"+getFollowers()+"\n";
    }
}
