package xin.framework.http.func;


import io.reactivex.functions.Function;
import xin.framework.http.output.BaseOutPut;

/**
 * @Description: ApiResult<T>转T

 */
public class OutputFunc<T> implements Function<BaseOutPut<T>, T> {
    public OutputFunc() {
    }

    @Override
    public T apply(BaseOutPut<T> response) throws Exception {

            return response.getData();

    }
}
