package com.Invoices.AutoInvoices.Repository;

import com.Invoices.AutoInvoices.Entity.AccessToken;

public interface AccessTokenHandlerInterface {

    public AccessToken get();

    public void set(AccessToken accessToken);
}
