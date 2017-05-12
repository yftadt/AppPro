package com.app.net.common;

import com.app.net.req.BasePager;
import com.app.net.res.Paginator;

/**
 * Created by javacoder on 2016/10/19.
 */

public abstract class BasePageManager extends BaseManager {

    private BasePager basePager;
    protected Paginator paginato;

    public BasePageManager(RequestBack requestBack) {
        super(requestBack);
        instanceReq(basePager);
    }

    protected void instanceReq(BasePager basePager) {
        this.basePager = basePager;
    }

    protected void setPaginator(Paginator paginato) {
        this.paginato = paginato;
    }

    public void setPagerRest() {
        basePager.setPage(1);
    }

    protected void setPagerAdd() {
        int page = basePager.getPage() + 1;
        basePager.setPage(page);
    }

}
