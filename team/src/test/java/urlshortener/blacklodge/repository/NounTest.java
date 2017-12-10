package urlshortener.blacklodge.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import static org.junit.Assert.assertEquals;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class NounTest {
    private EmbeddedDatabase db;
    private NounRepository repository;
    private JdbcTemplate jdbc;

    @Before
    public void setup() {
        db = new EmbeddedDatabaseBuilder().setType(HSQL)
                .addScript("schema-hsqldb.sql").addScript("data-hsqldb.sql").build();
        jdbc = new JdbcTemplate(db);
        repository = new NounRepositoryImpl(jdbc);
    }

    @Test
    public void getNoun() {
        String noun = repository.get(1);
        assertEquals(noun,jdbc.queryForObject("select word from nouns where id=1",String.class));
    }

    @Test
    public void getNumber() {
        assertEquals(repository.number(),new Integer(65232));
    }

    @Test
    public void nullAfterErrorGet() {
        assertEquals(null,repository.get(9999999));
    }

    @After
    public void shutdown() {
        db.shutdown();
    }
}
