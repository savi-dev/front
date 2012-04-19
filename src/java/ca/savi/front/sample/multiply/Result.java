// Copyright (c) 2012, The SAVI Project.
package ca.savi.front.sample.multiply;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A multiplication result.
 *
 * @author Soheil Hassas Yeganeh <soheil@cs.toronto.edu>
 */
@XmlRootElement
public class Result {
  @XmlElement
  List<String> operands;

  @XmlElement
  String result;

  public Result() {}

  public Result(List<String> operands, String result) {
    this.result = result;
    this.operands = operands;
  }
}
