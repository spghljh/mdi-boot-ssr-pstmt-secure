package kr.co.mdi.common.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DbProfileResolver {

    private final Environment env;

    public DbProfileResolver(Environment env) {
        this.env = env;
    }

    public String getDbType() {
        String[] profiles = env.getActiveProfiles();
        for (String profile : profiles) {
            if (profile.contains("postgre")) return "postgre";
            if (profile.contains("mysql")) return "MySQL";
            if (profile.contains("oracle")) return "Oracle";
        }
        return "Undefined";
    }
}
