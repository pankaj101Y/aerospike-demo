package com.example.aerospikeembedded;

import com.aerospike.client.*;
import com.aerospike.client.Record;
import com.aerospike.client.policy.ClientPolicy;
import com.example.aerospikeembedded.config.AerospikeCacheObject;
import com.example.aerospikeembedded.config.AerospikeCacheObjectTemp;
import com.example.aerospikeembedded.models.CustomerModelAS;
import com.example.aerospikeembedded.repo.CustomerAerospikeRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@Slf4j
class AerospikeEmbeddedApplicationTests {

    public static final String AEROSPIKE_NAMESPACE = "myspace";

    @Container
    public static GenericContainer aerospikeContainer = new GenericContainer("aerospike")
            .withExposedPorts(3000, 3001, 3002)
            .withEnv("NAMESPACE", AEROSPIKE_NAMESPACE)
            .withEnv("FEATURE_KEY_FILE", "/etc/aerospike/features.conf")
            .withClasspathResourceMapping("trial-features.conf",
                    "/etc/aerospike/features.conf",
                    BindMode.READ_ONLY)
            .waitingFor(Wait.forLogMessage(".*migrations: complete.*", 1));

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("aerospike.hosts", AerospikeEmbeddedApplicationTests::hosts);
    }

    private static String getIp(String ip, Integer port) {
        String res = ip + ":" + port;
        log.info("aerospike ip {}", res);
        return res;
    }

    @Autowired
    CustomerAerospikeRepo<CustomerModelAS> aerospikeRepo;

    @Autowired
    AerospikeClient aerospikeClient;

    @Test
    void contextLoads() {
        if (Boolean.parseBoolean(System.getProperty("cron.enabled"))){
            aerospikeRepo.save(AerospikeCacheObject.<CustomerModelAS>builder().data(new CustomerModelAS()).id(1L).build());

        }else{
            aerospikeRepo.save(new AerospikeCacheObjectTemp<>(1L, new CustomerModelAS()));
        }

        ClientPolicy policy = new ClientPolicy();
//        policy.user = params.user;
//        policy.password = params.password;
//        policy.authMode = params.authMode;
//        policy.tlsPolicy = params.tlsPolicy;

        Host[] hosts = Host.parseHosts(hosts(),3000);
        AerospikeClient client = new AerospikeClient(policy, hosts);


        client.scanAll(client.scanPolicyDefault, AEROSPIKE_NAMESPACE, AerospikeCacheObjectTemp.class.getSimpleName(), new ScanCallback() {
            @Override
            public void scanCallback(Key key, Record record) throws AerospikeException {
                log.info("key {} record {} ",key,record);
            }
        });

        client.scanAll(client.scanPolicyDefault, AEROSPIKE_NAMESPACE, AerospikeCacheObject.class.getSimpleName(), new ScanCallback() {
            @Override
            public void scanCallback(Key key, Record record) throws AerospikeException {
                log.info("key {} record {} ",key,record);
            }
        });

    }

    private static String hosts(){
        String ip = aerospikeContainer.getHost();
        return String.join(",", Arrays.asList(
                getIp(ip, aerospikeContainer.getMappedPort(3000)),
                getIp(ip, aerospikeContainer.getMappedPort(3001)),
                getIp(ip, aerospikeContainer.getMappedPort(3002))
        ));
    }

}
