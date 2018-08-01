package xin.framework.http.func;


import io.reactivex.functions.Function;
import xin.framework.http.output.BaseOutPut;

/**
 * @Description: ApiResult<T>转T

 */
public class OutputFunction<T> implements Function<BaseOutPut<T>, T> {
    public OutputFunction() {
    }

    @Override
    public T apply(BaseOutPut<T> response) {

            return response.getData();

    }
}
