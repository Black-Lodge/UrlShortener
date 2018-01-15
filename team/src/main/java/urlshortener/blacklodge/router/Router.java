package urlshortener.blacklodge.router;

import java.util.List;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import urlshortener.blacklodge.infrastructure.CSVprocessor;

/**
 * Class that implements the router class that Apache Camel needs to how to do the tasks assigned to it
 */
@Component
public class Router extends RouteBuilder {

  @Autowired
  CSVprocessor csvprocessor;

  /**
   * Configuration for the Apache Camel route that processes the CSV files
   */
  @Override
  public void configure() {
    from("direct:processCSV")
            //Unmarshal CSV files. The resulting message contains a List<List<String>>
            .unmarshal().csv()
            //Split the message into a number of pieces
            .split().body(List.class)
            //Process
            .process(csvprocessor);
  }

}