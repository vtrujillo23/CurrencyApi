package com.company.currency.integration;

import java.io.IOException;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public abstract class AbstractIntTest {
	private MongodExecutable mongodExecutable;
	private WireMockServer wireMockServer;

	protected void startMongoEmbbed() throws IOException {
		String ip = "localhost";
		int port = 27017;
		IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.DEVELOPMENT)
				.net(new Net(ip, port, Network.localhostIsIPv6())).build();
		MongodStarter starter = MongodStarter.getDefaultInstance();
		mongodExecutable = starter.prepare(mongodConfig);
		mongodExecutable.start();
	}


	protected void startWireMock() {
		wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
				.fileSource(new SingleRootFileSource("src/test/resources")).port(8009));
		wireMockServer.start();
	}

	protected void stopWireMock() {
		wireMockServer.stop();
	}
}
