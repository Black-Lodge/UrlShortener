package urlshortener.blacklodge.repository;

import static org.junit.Assert.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;
import static urlshortener.blacklodge.repository.Fixture.ShortURLFixtureTest.url1;
import static urlshortener.blacklodge.repository.Fixture.ShortURLFixtureTest.url2;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.repository.ShortURLRepository;

/**
 * Tests for the Short URL repo
 */
public class ShortURLRepoTest {

  private EmbeddedDatabase db;
  private ShortURLRepository repository;
  private JdbcTemplate jdbc;

  @Before
  public void setup() {
    db = new EmbeddedDatabaseBuilder().setType(HSQL)
            .addScript("schema-hsqldb.sql").build();
    jdbc = new JdbcTemplate(db);
    repository = new ShortURLRepo(jdbc);
  }

  /**
   * Tests that saving a URL is persistent
   */
  @Test
  public void thatSavePersistsTheShortURL() {
    assertNotNull(repository.save(url1()));
    assertSame(jdbc.queryForObject("select count(*) from SHORTURL",
            Integer.class), 1);
  }

  /**
   * Tests that saving a duplicated hash returns a null and doesn't save anything
   */
  @Test
  public void thatSaveADuplicateHashIsSafelyIgnored() {
    repository.save(url1());
    assertNull(repository.save(url1()));
    assertSame(jdbc.queryForObject("select count(*) from SHORTURL",
            Integer.class), 1);
  }

  /**
   * Tests that search by key works as expected
   */
  @Test
  public void thatFindByKeyReturnsAURL() {
    ShortURL su = repository.save(url1());
    assertNotNull(su);
    su = repository.save(url2());
    assertNotNull(su);
    su = repository.findByKey(url1().getHash());
    assertNotNull(su);
    assertSame(su.getHash(), url1().getHash());
  }

}
