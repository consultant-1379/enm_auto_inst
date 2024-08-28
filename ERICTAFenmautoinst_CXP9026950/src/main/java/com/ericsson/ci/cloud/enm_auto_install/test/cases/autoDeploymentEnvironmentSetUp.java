package com.ericsson.ci.cloud.enm_auto_install.test.cases;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.ci.cloud.enm_auto_install.operators.EnvironmentSetUpOperator;

public class autoDeploymentEnvironmentSetUp extends TorTestCaseHelper implements TestCase {

    @Inject
    OperatorRegistry<EnvironmentSetUpOperator> operatorRegistry;

    private EnvironmentSetUpOperator getOperator() {
        return operatorRegistry.provide(EnvironmentSetUpOperator.class);
    }

    @Context(context = { Context.CLI })
    @Test
    public void verifyautoDeploymentInstallation() {
        assertTrue(getOperator().autoDeploymentInstallation());
    }
}
