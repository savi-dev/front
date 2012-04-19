// Copyright (c) 2012, The SAVI Project.
package ca.savi.front;

import java.io.IOException;

import ca.savi.front.connector.Connector;
import ca.savi.front.connector.ConnectorFactory;

/**
 * FrontServer is the main class responsible for bootstrapping SAVI front-end
 * server. The current implementation is based on Grizzly and supports JAX-RS,
 * Atmosphere, and JAX-WS.
 *
 * You can simply use it programmatically:
 * <pre>
 * {@code
 * FrontServer f = new FrontServer("localhost", 8080, false);
 * f.register(SomeJAXRS.class, SomeJaxWS.class, SomeAtmoshphere.class);
 * f.start();
 * System.in.read();
 * f.stop();e
 * }
 * </pre>
 *
 * @author Soheil Hassas Yeganeh <soheil@cs.toronto.edu>
 * @version 0.1
 */
// TODO(soheil): Add unit tests.
public class FrontServer {
  /**
   * The active connector.
   */
  private final Connector connector;

  /**
   * Initializes the server.
   *
   * @param listenAddress The listen address.
   * @param port The port number.
   * @param isSecure Whether to use SSL or not.
   */
  public FrontServer(String listenAddress, int port, boolean isSecure) {
    connector = ConnectorFactory.getConnector();
    connector.init(listenAddress, port, isSecure);
  }

  /**
   * The set of JAX-RS, and JAX-WS classes. It is safe to call it multiple
   * times.
   *
   * @param classes The classes to register.
   */
  public void register(Class<?>... classes) {
    connector.register(classes);
  }

  /**
   * Starts the server.
   * @throws IOException When some IO error occurred.
   */
  public void start() throws IOException {
    connector.start();
  }

  /**
   * Stops the server.
   * @throws IOException When some IO error occurred.
   */
  public void stop() throws IOException {
    connector.stop();
  }
}
