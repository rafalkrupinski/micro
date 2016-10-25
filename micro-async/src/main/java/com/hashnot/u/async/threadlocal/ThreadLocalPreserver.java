package com.hashnot.u.async.threadlocal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Rafał Krupiński
 */
public class ThreadLocalPreserver {
    final private static Logger log = LoggerFactory.getLogger(ThreadLocalPreserver.class);
    final private static Set<ThreadLocal> threadLocals = new HashSet<>();

    public static State getState() {
        State result = new State(threadLocals.stream().filter(l -> l.get() != null).collect(Collectors.toMap(Function.identity(), ThreadLocal::get)));
        log.debug("get state {}", result.state.size());
        return result;
    }

    public static void setState(State state) {
        log.debug("set state {}", state.state.size());
        state.state.entrySet().forEach(e -> e.getKey().set(e.getValue()));
    }

    public static void register(ThreadLocal threadLocal) {
        threadLocals.add(threadLocal);
    }

    public static void clean() {
        log.debug("clean state");
        threadLocals.forEach(ThreadLocal::remove);
    }

    static class State {
        private Map<ThreadLocal, Object> state;

        State(Map<ThreadLocal, Object> state) {
            this.state = state;
        }
    }
}
