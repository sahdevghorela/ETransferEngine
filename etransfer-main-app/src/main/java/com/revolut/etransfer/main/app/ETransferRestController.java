package com.revolut.etransfer.main.app;

import com.revolut.etransfer.dao.impl.InMemoryAccountManagementDaoImpl;
import com.revolut.etransfer.domain.exception.AccountNotFoundException;
import com.revolut.etransfer.domain.model.Account;
import com.revolut.etransfer.service.AccountManagementService;
import com.revolut.etransfer.service.impl.AccountManagementServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Optional;

@Path("/")
public class ETransferRestController {

    private static final AccountManagementService accountService = new AccountManagementServiceImpl(new InMemoryAccountManagementDaoImpl());

    @Path("account/create")
    @POST
    public Response createAccount(@QueryParam("balance") String balance) {
        Account account = null;
        if (balance == null) {
            account = accountService.create();
        } else {
            try {
                BigDecimal amount = new BigDecimal(balance);
                account = accountService.create(amount);

            } catch (NumberFormatException e) {
                Response.status(Response.Status.BAD_REQUEST).entity("wrong number format, " + balance).build();
            }
        }
        return Response
                .status(Response.Status.CREATED)
                .entity("Account number created " + account.getAccountNumber() + " with balance " + account.getBalance())
                .build();
    }

    @Path("account/find/{accountNumber}")
    @GET
    public Response findAccount(@PathParam("accountNumber") String accountNumber) {
        try {
            Optional<Account> account = accountService.find(Long.parseLong(accountNumber));
            if (account.isPresent()) {
                return Response.ok("Account " + account.get().getAccountNumber() + ",Balance " + account.get().getBalance()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Account does not exist").build();
            }

        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("wrong number format, " + accountNumber).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("account/delete/{accountNumber}")
    @DELETE
    public Response delete(@PathParam("accountNumber") String accountNumber) {
        try {
            accountService.delete(Long.parseLong(accountNumber));
            return Response.ok("Account " + accountNumber + " deleted successfully").build();

        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("wrong number format, " + accountNumber).build();
        } catch (AccountNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("account/transfer")
    @PUT
    public Response transfer(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("amount") String amount) {
        try {
            final long fromAccountId = Long.parseLong(from);
            final long toAccountId = Long.parseLong(to);
            final BigDecimal value = new BigDecimal(amount);
            accountService.transfer(fromAccountId, toAccountId, value);
            return Response.ok("Transfer from " + from + " to " + to + " for " + amount + " successful").build();
        } catch (AccountNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
