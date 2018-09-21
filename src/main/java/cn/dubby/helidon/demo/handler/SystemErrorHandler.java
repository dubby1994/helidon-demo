package cn.dubby.helidon.demo.handler;

import io.helidon.webserver.ErrorHandler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;

import javax.json.Json;
import javax.json.JsonObject;

public class SystemErrorHandler implements ErrorHandler<RuntimeException> {
    @Override
    public void accept(ServerRequest req, ServerResponse res, RuntimeException ex) {
        JsonObject returnObject = Json.createObjectBuilder()
                .add("message", "system error")
                .add("code", -1)
                .build();
        res.send(returnObject);
    }
}
