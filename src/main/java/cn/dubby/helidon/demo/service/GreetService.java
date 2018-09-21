/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.dubby.helidon.demo.service;

import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;
import io.prometheus.client.Counter;

import javax.json.Json;
import javax.json.JsonObject;

public class GreetService implements Service {

    private static final Counter greetingCounter = Counter.build().name("greeting_total").help("Total request").register();

    private static final Config CONFIG = Config.create().get("app");

    private static String greeting = CONFIG.get("greeting").asString("Ciao");

    @Override
    public final void update(final Routing.Rules rules) {
        rules
                .get("/", this::getDefaultMessage)
                .get("/{name}", this::getMessage)
                .put("/greeting/{greeting}", this::updateGreeting);
    }

    private void getDefaultMessage(final ServerRequest request, final ServerResponse response) {
        String msg = String.format("%s %s!", greeting, "World");
        JsonObject returnObject = Json.createObjectBuilder()
                .add("message", msg)
                .build();
        response.send(returnObject);
        greetingCounter.inc();
    }

    private void getMessage(final ServerRequest request, final ServerResponse response) {
        String name = request.path().param("name");
        String msg = String.format("%s %s!", greeting, name);

        JsonObject returnObject = Json.createObjectBuilder()
                .add("message", msg)
                .build();
        response.send(returnObject);
        greetingCounter.inc();
    }

    private void updateGreeting(final ServerRequest request, final ServerResponse response) {
        greeting = request.path().param("greeting");

        JsonObject returnObject = Json.createObjectBuilder()
                .add("greeting", greeting)
                .build();
        response.send(returnObject);
        greetingCounter.inc();
    }
}
