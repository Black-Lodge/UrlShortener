package urlshortener.blacklodge.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import static org.junit.Assert.assertEquals;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

/**
 * Tests the adj repo
 */
public class AdjTest {
    private EmbeddedDatabase db;
    private AdjRepository repository;
    private JdbcTemplate jdbc;

    @Before
    public void setup() {
        db = new EmbeddedDatabaseBuilder().setType(HSQL)
                .addScript("schema-hsqldb.sql").addScript("data-hsqldb.sql").build();
        jdbc = new JdbcTemplate(db);
        repository = new AdjRepositoryImpl(jdbc);
    }

    /**
     * Tests that the get function works as expected
     */
    @Test
    public void getAdj() {
        String adj = repository.get(1);
        assertEquals(adj,jdbc.queryForObject("select word from adj where id=1",String.class));
    }

    /**
     * Tests that the getNumber function works as expected
     */
    @Test
    public void getNumber() {
        assertEquals(repository.number(),new Integer(11947));
    }

    /**
     * Tests that a null string is returned if something is wrong
     */
    @Test
    public void nullAfterErrorGet() {
        assertEquals(null,repository.get(9999999));
    }

    @After
    public void shutdown() {
        db.shutdown();
    }
}
