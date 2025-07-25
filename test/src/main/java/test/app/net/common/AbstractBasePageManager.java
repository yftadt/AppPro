package test.app.net.common;


import com.retrofits.net.common.RequestBack;

import test.app.net.req.BasePager;
import test.app.net.res.Paginator;

/**
 * Created by javacoder on 2016/10/19.
 */

public abstract class AbstractBasePageManager extends AbstractBaseManager {

    private BasePager basePager;
    protected Paginator paginato;

    public AbstractBasePageManager(RequestBack requestBack) {
        super(requestBack);
    }



    protected void setBasePager(BasePager basePager) {
        this.basePager = basePager;
        setBaseReq(basePager);
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

    public int getPageIndex() {
        int page = basePager.getPage();
        return page;
    }

}
