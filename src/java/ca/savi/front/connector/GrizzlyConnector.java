// Copyright (c) 2012, The SAVI Project.
package ca.savi.front.connector;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.jws.WebService;
import javax.ws.rs.Path;
import javax.xml.ws.Endpoint;
import javax.xml.ws.spi.http.HttpContext;

import org.apache.commons.lang.StringUtils;
import org.atmosphere.grizzly.AtmosphereAdapter;
import
    org.jvnet.jax_ws_commons.transport.grizzly_httpspi.GrizzlyHttpContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.grizzly.comet.CometAsyncFilter;
import com.sun.grizzly.http.embed.GrizzlyWebServer;

/**
 * A simple Grizzly connector with metro, jersey, and atmosphere support.
 * Code is partially grabbed from org.atmosphere.grizzly.AtmosphereSpadeServer.
 *
 * @author Soheil Hassas Yeganeh <soheil@cs.toronto.edu>
 * @version 0.1
 */
public class GrizzlyConnector implements Connector {

  private static final Logger logger =
      LoggerFactory.getLogger(GrizzlyConnector.class);

  private AtmosphereAdapter atmosphereAdaptor = new AtmosphereAdapter();
  private boolean defaultToJersey = true;
  private GrizzlyWebServer gws;
  private Set<String> jaxRsPackages = new HashSet<String>();
  private Set<Class<?>> jaxWsClasses = new HashSet<Class<?>>();

  @Override
  public Connector init(String listenAddress, int port, boolean isSecure) {
    gws = new GrizzlyWebServer(port, ".", isSecure);
    gws.getSelectorThread().setDisplayConfiguration(false);
    gws.addAsyncFilter(new CometAsyncFilter());
    return this;
  }

  @Override
  public Connector register(Class<?>... classes) {
    addJaxRsPackages(classes);
    addJaxWsClasses(classes);
    return this;
  }

  /**
   * Finds JAX-WS classes, and adds them to the jaxWsPackages.
   *
   * @param classes The list of classes to search.
   */
  private void addJaxWsClasses(Class<?>[] classes) {
    for (Class<?> clazz : classes) {
      if (clazz.getAnnotation(WebService.class) != null) {
        jaxWsClasses.add(clazz);
      }
    }
  }

  /**
   * Finds JAX-RS packages from the classes, and adds them to the jaxRsPackages.
   *
   * @param classes The list of classes to search.
   */
  private void addJaxRsPackages(Class<?>... classes) {
    for (Class<?> clazz : classes) {
      if (clazz.getAnnotation(Path.class) != null) {
        jaxRsPackages.add(clazz.getPackage().getName());
      }
    }
  }

  @Override
  public Connector start() throws IOException {
    logger.info("Front server Started on port: {}",
        gws.getSelectorThread().getPort());
    String resourcePackages = StringUtils.join(jaxRsPackages, ';');
    logger.debug("Loading RESTFul services from the following packages: {}",
        resourcePackages);
    atmosphereAdaptor.setResourcePackage(resourcePackages);

    if (defaultToJersey) {
      atmosphereAdaptor.setHandleStaticResources(true);
      gws.addGrizzlyAdapter(atmosphereAdaptor, new String[]{"*"});
    }

    logger.debug("Loading SOAP services from the following packages: {}",
        resourcePackages);
    for (Class<?> clazz : jaxWsClasses) {
      WebService ws = clazz.getAnnotation(WebService.class);
      String name = !StringUtils.isEmpty(ws.name()) ? ws.name() :
        clazz.getSimpleName();
      HttpContext context = GrizzlyHttpContextFactory.createHttpContext(gws,
          "/ws", "/" + name);
      Endpoint endpoint;
        try {
          endpoint = Endpoint.create(clazz.newInstance());
          publish(endpoint, context);
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    gws.start();
    return this;
  }

  /**
   * @param endpoint
   * @param context
   */
  private void publish(Endpoint endpoint, HttpContext context) {
      Method publishMethod;
      try {
        publishMethod = endpoint.getClass().getDeclaredMethod("publish",
            HttpContext.class);
        publishMethod.invoke(endpoint, context);
        return;
      } catch (Exception e) {
      }
      endpoint.publish(context);
  }

  @Override
  public Connector stop() throws IOException {
    gws.stop();
    return this;
  }

}
