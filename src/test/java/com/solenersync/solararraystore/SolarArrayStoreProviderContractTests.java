package com.solenersync.solararraystore;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.*;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.solenersync.solararraystore.repository.SolarArrayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("solar-array-store")
@Consumer("ses-front-end")
//@PactBroker(url = "https://solenersync.pactflow.io", authentication = @PactBrokerAuth(token = "${PACT_BROKER_TOKEN}"))
@PactFolder("pacts")
@IgnoreNoPactsToVerify
@IgnoreMissingStateChange
@ExtendWith(SpringExtension.class)
@ActiveProfiles("pact-provider")
public class SolarArrayStoreProviderContractTests {

    @MockBean
    SolarArrayRepository solarArrayRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup(PactVerificationContext context) {
        if (context != null) {
            context.setTarget(new HttpTestTarget("localhost", port));
        }
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }

    @State("a solar array exists")
    void getUserSolarArray() {
        StubSetup.stubForGetSolarArray(solarArrayRepository);
    }

    @State("a solar array doesnt exist")
    void getUserSolarArrayFail() {
        StubSetup.stubForGetSolarArrayFail(solarArrayRepository);
    }

    @State("a solar array is created")
    void createUser() {
        StubSetup.stubForCreateSolarArray(solarArrayRepository);
    }

    @State("a solar array fails to be created")
    void createUserFail() {
        StubSetup.stubForCreateSolarArrayFail(solarArrayRepository);
    }

    @State("a solar array is updated")
    void updateUser() {
        StubSetup.stubForUpdateSolarArray(solarArrayRepository);
    }

    @State("a solar array fails to update")
    void updateUserFail() {
        StubSetup.stubForUpdateSolarArray(solarArrayRepository);
    }

}
