package test.app.net.res;

import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultObject<T> extends BaseResult {
    public T obj;
    public List<T> list;


}
