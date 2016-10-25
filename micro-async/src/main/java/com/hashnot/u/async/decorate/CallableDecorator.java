package com.hashnot.u.async.decorate;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * @author Rafał Krupiński
 */
@FunctionalInterface
public interface CallableDecorator<ResultT> extends Function<Callable<ResultT>, Callable<ResultT>>  {

}
