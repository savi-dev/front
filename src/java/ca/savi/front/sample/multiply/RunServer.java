// Copyright (c) 2012, The SAVI Project.
package ca.savi.front.sample.multiply;

import java.io.IOException;

import ca.savi.front.FrontServer;

/**
 * Runs a server on port 9876.
 *
 * @author Soheil Hassas Yeganeh <soheil@cs.toronto.edu>
 */
public class RunServer {
  public static void main(String[] args) throws IOException {
    FrontServer fs = new FrontServer("0.0.0.0", 9876, false);
    fs.register(Multiply.class);
    fs.start();
  }
}
