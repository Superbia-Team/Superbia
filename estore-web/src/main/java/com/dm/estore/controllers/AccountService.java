package com.dm.estore.controllers;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * User: denis
 * Date: 11/1/13
 * Time: 10:49 PM
 */
@Component
@Path("/account")
public class AccountService {

    @GET
    @Path("/")
    public Response currentAccount() {
        String test = "TEST";

        return Response.ok(test).build();
    }
}
