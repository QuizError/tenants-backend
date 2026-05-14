package tz.co.divinesolutions.tenants_backend.globals;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> {

    private Boolean status;
    private Integer code;
    private String message;
    private T data;
    private List<T> dataList;

    public Response(boolean status, Integer code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(boolean status, Integer code, List<T> dataList, String message) {
        this.status = status;
        this.code = code;
        this.dataList = dataList;
        this.message = message;
    }
}