// Copyright (c) 2012, The SAVI Project.
package ca.savi.front.connector;

/**
 * Factory class for Connectors.
 *
 * @author Soheil Hassas Yeganeh <soheil@cs.toronto.edu>
 * @version 0.1
 */
public class ConnectorFactory {
  /**
   * The default front server connector.
   */
  private static final String DEFAULT_CONNECTOR =
      GrizzlyConnector.class.getName();

  /**
   * @return The default connector.
   */
  public static Connector getConnector() {
    return getConnector(DEFAULT_CONNECTOR);
  }

  /**
   * Loads the connector.
   *
   * @param className Connector's class name.
   * @return Connector instance.
   */
  public static Connector getConnector(String className) {
    try {
      return (Connector) Class.forName(className).newInstance();
    } catch (Exception e) {
      return null;
    }
  }
}
