package urlshortener.blacklodge.repository;

import org.glassfish.grizzly.Grizzly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.logging.Logger;

/**
 * Class that controls how nouns are searched on the database
 */
@Repository
public class NounRepositoryImpl implements NounRepository {

  @Autowired
  protected JdbcTemplate jdbc;
  private static final Logger log = Grizzly.logger(NounRepositoryImpl.class);

  public NounRepositoryImpl(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  /**
   * Looks for a noun based on its id
   * @param id id of the noun to retrieve
   * @return The noun that was found or a null string otherwise
   */
  @Override
  public String get(int id) {
    try {
      return jdbc.queryForObject("select word from nouns where id=?",String.class,id);
    } catch (Exception e) {
      log.info("When getting noun with id "+id+": "+e.getMessage());
      return null;
    }
  }

  /**
   * Returns the number of nouns on the database
   * @return Number of words
   */
  @Override
  public Integer number() {
    try {
      return jdbc
              .queryForObject("select count(*) from nouns", Integer.class);
    } catch (Exception e) {
      log.info("When counting"+e.getMessage());
    }
    return -1;
  }

}
