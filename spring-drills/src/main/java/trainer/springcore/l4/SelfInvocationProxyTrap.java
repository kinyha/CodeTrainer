package trainer.springcore.l4;

import org.springframework.stereotype.Component;

// @task springcore.l4.SelfInvocationProxyTrap
// @tags spring-core,aop,proxy,self-invocation,pitfall
// @time 30m
// @src  new
@Component
public class SelfInvocationProxyTrap {

    @Timed
    public String outer(String name) {
        // ---8<--- solution
        return inner(name); // WHY: this.inner(...) идёт мимо proxy — advice на inner() не сработает
        // --->8--- solution
    }

    @Timed
    public String inner(String name) {
        return "hi " + name;
    }
}
