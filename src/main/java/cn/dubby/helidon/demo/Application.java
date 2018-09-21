package cn.dubby.helidon.demo;

import cn.dubby.helidon.demo.handler.HttpExceptionHandler;
import cn.dubby.helidon.demo.handler.SystemErrorHandler;
import cn.dubby.helidon.demo.service.ExceptionService;
import cn.dubby.helidon.demo.service.GreetService;
import io.helidon.config.Config;
import io.helidon.webserver.*;
import io.helidon.webserver.json.JsonSupport;
import io.helidon.webserver.prometheus.PrometheusSupport;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.LogManager;

public class Application {

    private static Routing createRouting() {
        return Routing.builder()
                .register(JsonSupport.get())
                .register(PrometheusSupport.create())
                //静态资源
                .register("/", StaticContentSupport.builder(Paths.get("/Users/dubby/Desktop/index"))
                        .welcomeFileName("index.html")
                        .build())
                .register("/picture", StaticContentSupport.create(Paths.get("/Users/dubby/Desktop/pic")))
                //正常的get,put
                .register("/greet", new GreetService())
                //error handler
                .register("/exception", new ExceptionService())
                .error(HttpException.class, new HttpExceptionHandler())
                .error(RuntimeException.class, new SystemErrorHandler())
                .build();
    }

    public static void main(final String[] args) throws IOException {
        startServer();
    }

    static WebServer startServer() throws IOException {
        LogManager.getLogManager().readConfiguration(Application.class.getResourceAsStream("/logging.properties"));
        Config config = Config.create();

        ServerConfiguration configuration = ServerConfiguration.builder()
                .config(config.get("server"))
                .build();

        WebServer server = WebServer.create(configuration, createRouting());
        server.start().thenAccept(ws -> {
            System.out.println("WEB server is up! http://localhost:" + ws.port());
        });
        // server线程不是守护线程，所以不需要堵塞，直接执行就行
        server.whenShutdown().thenRun(() -> System.out.println("WEB server is DOWN. Good bye!"));
        return server;
    }

}
