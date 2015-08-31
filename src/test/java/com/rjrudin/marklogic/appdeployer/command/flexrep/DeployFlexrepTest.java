package com.rjrudin.marklogic.appdeployer.command.flexrep;

import java.io.File;

import org.junit.Test;

import com.rjrudin.marklogic.appdeployer.AbstractAppDeployerTest;
import com.rjrudin.marklogic.appdeployer.command.cpf.DeployCpfConfigsCommand;
import com.rjrudin.marklogic.appdeployer.command.cpf.DeployDomainsCommand;
import com.rjrudin.marklogic.appdeployer.command.cpf.DeployPipelinesCommand;
import com.rjrudin.marklogic.appdeployer.command.databases.DeployContentDatabasesCommand;
import com.rjrudin.marklogic.appdeployer.command.databases.DeployTriggersDatabaseCommand;

public class DeployFlexrepTest extends AbstractAppDeployerTest {

    @Test
    public void test() {
        appConfig.getConfigDir().setBaseDir(new File("src/test/resources/sample-app/flexrep-config"));

        initializeAppDeployer(new DeployContentDatabasesCommand(), new DeployTriggersDatabaseCommand(),
                new DeployCpfConfigsCommand(), new DeployDomainsCommand(), new DeployPipelinesCommand(),
                new DeployFlexrepConfigCommand(), new DeployTargetsCommand("sample-app-domain-1"));

        appDeployer.deploy(appConfig);
    }
}
