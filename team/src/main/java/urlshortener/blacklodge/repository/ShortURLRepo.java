package urlshortener.blacklodge.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.repository.ShortURLRepositoryImpl;

/**
 * Class that extends the class that controls the persistence of the shorten URLs
 */
@Repository
public class ShortURLRepo extends ShortURLRepositoryImpl {

  private static final Logger log = LoggerFactory.getLogger(ShortURLRepo.class);

  private static final RowMapper<ShortURL> rowMapper = (rs, rowNum) -> new ShortURL(rs.getString("hash"),
          rs.getString("target"), null, rs.getString("sponsor"), rs.getDate("created"),
          rs.getString("owner"), rs.getInt("mode"),
          rs.getBoolean("safe"), rs.getString("ip"),
          rs.getString("country"),rs.getString("image"));

  public ShortURLRepo(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  /**
   * Stores the URL on the database
   * @param su Short URL to save
   * @return Saved URL is everything went fine, null url otherwise
   */
  @Override
  public ShortURL save(ShortURL su) {
    try {
      jdbc.update("INSERT INTO shorturl VALUES (?,?,?,?,?,?,?,?,?,?)",
              su.getHash(), su.getTarget(), su.getSponsor(),
              su.getCreated(), su.getOwner(), su.getMode(), su.getSafe(),
              su.getIP(), su.getCountry(),su.getImage());
    } catch (DuplicateKeyException e) {
      log.debug("When insert for key " + su.getHash(), e);
      return null;
    } catch (Exception e) {
      log.debug("When insert", e);
      return null;
    }
    return su;
  }

  /**
   * Looks for a url based on the hash
   * @param id Hash that is used to search
   * @return url if it was find. null url otherwise
   */
  @Override
  public ShortURL findByKey(String id) {
    try {
      return jdbc.queryForObject("SELECT * FROM shorturl WHERE hash=?",
              rowMapper, id);
    } catch (Exception e) {
      log.debug("When select for key " + id, e);
      return null;
    }
  }

}
