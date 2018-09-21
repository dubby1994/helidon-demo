package cn.dubby.helidon.demo.handler;

import io.helidon.webserver.ErrorHandler;
import io.helidon.webserver.HttpException;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;

public class HttpExceptionHandler implements ErrorHandler<HttpException> {
    @Override
    public void accept(ServerRequest req, ServerResponse res, HttpException ex) {
        res.status(ex.status());
        res.send(ex.getMessage());
    }
}
