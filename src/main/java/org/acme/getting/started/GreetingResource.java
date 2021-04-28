package org.acme.getting.started;

import javax.validation.Valid;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/")
public class GreetingResource {

    @PUT
    @Path("/passive")
    public Boolean issue(@Valid Operator<Issue> parameter) {
        System.out.printf("Issue here: ", parameter.data.getAlive());
        return parameter.data.getAlive();
    }
}