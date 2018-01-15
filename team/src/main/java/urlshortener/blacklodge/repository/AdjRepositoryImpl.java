package urlshortener.blacklodge.repository;

import org.glassfish.grizzly.Grizzly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.logging.Logger;

/**
 * Class that controls how adjectives are searched on the database
 */
@Repository
public class AdjRepositoryImpl implements AdjRepository {

  @Autowired
  protected JdbcTemplate jdbc;
  private static final Logger log = Grizzly.logger(AdjRepositoryImpl.class);

  public AdjRepositoryImpl(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  /**
   * Looks for an adj based on its id
   * @param id id of the adj to retrieve
   * @return The adj that was found or a null string otherwise
   */
  @Override
  public String get(int id) {
    try {
      return jdbc.queryForObject("select word from adj where id=?",String.class,id);
    } catch (Exception e) {
      log.info("When getting adj with id "+id+": "+e.getMessage());
      return null;
    }
  }

  /**
   * Returns the number of adjs on the database
   * @return Number of adjs
   */
  @Override
  public Integer number() {
    try {
      return jdbc
              .queryForObject("select count(*) from adj", Integer.class);
    } catch (Exception e) {
      log.info("When counting"+e.getMessage());
    }
    return -1;
  }

}
