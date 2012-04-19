// Copyright (c) 2012, The SAVI Project.
package ca.savi.front.sample.multiply;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * A multiplication service.
 *
 * @author Eliot Kang <eliot@savinetwork.ca>
 */
@Path("/{left}/{right}")
@WebService
public class Multiply {
  @GET
  @Produces({"application/json"})
  public Result multiply(@PathParam("left") String left,
      @PathParam("right") String right) {
    Integer resultValue = Integer.valueOf(left) * Integer.valueOf(right);
    List<String> operands = new ArrayList<String>();
    operands.add(left);
    operands.add(right);
    return new Result(operands, resultValue.toString());
  }
}
