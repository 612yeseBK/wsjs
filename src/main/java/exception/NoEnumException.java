package exception;

/**
 * description: 前后端域名匹配失败的异常
 * Created by gaoyw on 2018/4/15.
 */
public class NoEnumException extends Exception {
    public NoEnumException(String msg){
        super(msg);
    }
}
