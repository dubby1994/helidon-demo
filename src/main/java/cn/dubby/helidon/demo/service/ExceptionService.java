package cn.dubby.helidon.demo.service;

import io.helidon.common.http.Http;
import io.helidon.webserver.*;

public class ExceptionService implements Service {

    @Override
    public void update(Routing.Rules rules) {
        rules.get("/error", this::error)
        .get("/404", this::httpError404);
    }

    private void error(final ServerRequest request, final ServerResponse response) {
        throw new RuntimeException("error");
    }

    private void httpError404(final ServerRequest request, final ServerResponse response) {
        throw new HttpException("404", Http.Status.NOT_FOUND_404);
    }

}
