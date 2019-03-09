package entity;

/**
 * 公共结果返回实体类
 */
public class Result {

    /**
     * true:成功  false：失败
     */
    public boolean flag;

    /**
     * 返回码 前端调用后端成功还是失败 返回码
     * 成功：20001
     * 失败：20002 2003 ...
     *
     * new Result(StatusCode.OK,xxxx.....);
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;
    /**
     * 具体返回的数据
     */
    private Object data;

    public Result() {
    }

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
