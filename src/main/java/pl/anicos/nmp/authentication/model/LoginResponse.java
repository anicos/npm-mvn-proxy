package pl.anicos.nmp.authentication.model;

public class LoginResponse {
    private Boolean ok;
    private String id;
    private String rev;
    private String token;

    public LoginResponse(Boolean ok, String id, String rev, String token) {
        this.ok = ok;
        this.id = id;
        this.rev = rev;
        this.token = token;
    }

    public Boolean getOk() {
        return ok;
    }

    public String getId() {
        return id;
    }

    public String getRev() {
        return rev;
    }

    public String getToken() {
        return token;
    }
}
