/*
 * Copyright (C) 2016-2024 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package servlet;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import testsupport.WireMockTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.lang.Thread.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AlternativeServletContainerTest {

  @RegisterExtension
  public static WireMockExtension wm =
      WireMockExtension.newInstance()
          .options(options().dynamicPort().httpServerFactory(new AltHttpServerFactory()))
          .build();

  private WireMockTestClient client;

  @BeforeEach
  public void init() {
    client = new WireMockTestClient(wm.getPort());
    WireMock.configureFor(wm.getPort());
    System.out.println(wm.baseUrl());
  }

  @Test
  public void supportsAlternativeHttpServerForBasicStub() {
    stubFor(get(urlEqualTo("/alt-server")).willReturn(aResponse().withStatus(204)));

    int statusCode = client.get("/alt-server").statusCode();
    System.out.println("Статус код в ответе: " + statusCode);
    assertThat(statusCode, is(204));
      try {
          sleep(32_000);
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }
  }
}
