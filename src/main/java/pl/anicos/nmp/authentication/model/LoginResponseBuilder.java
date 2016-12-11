package pl.anicos.nmp.authentication.model;

public class LoginResponseBuilder {
    private Boolean ok;
    private String id;
    private String rev;
    private String token;

    public LoginResponseBuilder setOk(Boolean ok) {
        this.ok = ok;
        return this;
    }

    public LoginResponseBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public LoginResponseBuilder setRev(String rev) {
        this.rev = rev;
        return this;
    }

    public LoginResponseBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponse build() {
        return new LoginResponse(ok, id, rev, token);
    }
}