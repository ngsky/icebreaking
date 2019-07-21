package com.ngsky.ice.data;

import com.ngsky.ice.data.server.CellServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@SpringBootApplication
public class IceDataApp implements CommandLineRunner {

    @Autowired
    private CellServer cellServer;

    @Value("${cell.port}")
    private int cellPort;

    public static void main(String[] args) {
        SpringApplication.run(IceDataApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        InetSocketAddress address = new InetSocketAddress("localhost", cellPort);
        System.out.println("run  .... . ... " + "localhost");
        cellServer.start(address);
    }
}
