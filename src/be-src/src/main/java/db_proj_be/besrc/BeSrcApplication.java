package db_proj_be.besrc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@SpringBootApplication
public class BeSrcApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(BeSrcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String sql = "SELECT * FROM PET";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        for(Map<String, Object> result : results) {
            System.out.println(result);
        }
    }
}
