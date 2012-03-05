// Copyright (c) 2012, The SAVI Project.
package ca.savi.front.connector;

import java.io.IOException;

/**
 * Connectors connect the underlying web server to the FrontServer.
 *
 * @author Soheil Hassas Yeganeh <soheil@cs.toronto.edu>
 * @version 0.1
 */
public interface Connector {
  /**
   * Initializes the connector.
   *
   * @param listenAddress The address on which the server should listen.
   * @param port The port on which the server should listen.
   * @param isSecure whether to use HTTPS or HTTP.
   * @return The connector.
   */
  Connector init(String listenAddress, int port, boolean isSecure);

  /**
   * Registers the set of classes which can be either JAX-WS or JAX-RS beans.
   *
   * @param classes The list of classes to register.
   * @return The connector.
   */
  Connector register(Class<?>... classes);

  /**
   * Starts the web server.
   *
   * @return The connector.
   */
  Connector start() throws IOException;

  /**
   * Stops the web server.
   *
   * @return The connector.
   */
  Connector stop() throws IOException;
}
