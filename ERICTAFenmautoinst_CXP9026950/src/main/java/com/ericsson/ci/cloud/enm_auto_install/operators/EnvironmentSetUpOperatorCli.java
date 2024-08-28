package com.ericsson.ci.cloud.enm_auto_install.operators;

import org.apache.log4j.Logger;

import com.ericsson.ci.cloud.enm_auto_install.getters.EnvironmentSetUpGetter;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.cli.CLITool;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.cifwk.taf.tools.cli.jsch.JSchCLIToolException;

@Operator(context = { Context.CLI })
public class EnvironmentSetUpOperatorCli implements EnvironmentSetUpOperator {

    private static final CLI cli = new CLI(DataHandler.getHostByName("gateway"));

    private static final Logger logger = Logger.getLogger(EnvironmentSetUpOperatorCli.class); 
    
    private static final String EXIT_CODE = "EXIT_CODE:";

    private boolean runCommand(String command, Long timeOut) {
        logger.info("Running "+ command +" cmd");
        Shell runShell = cli.executeCommand(command, "echo \"" + EXIT_CODE + "\"$?");

        String output = null;
        while (!runShell.isClosed() && timeOut > 0) {
            output = runShell.read();
            if (output.length() > 1){
                logger.debug(output);
            }
            timeOut -= CLITool.DEFAULT_TIMEOUT_SEC;
        }

        String lastLine = runShell.read();
        short result = -1;
        try {
            if (lastLine.length() < EXIT_CODE.length())
                lastLine = output;
            String exitCode = lastLine.split(EXIT_CODE)[1].trim();
            result = Short.valueOf(exitCode);
        } catch (JSchCLIToolException | IndexOutOfBoundsException e) {
            logger.error("Cannot get command result", e);

        }
        logger.info("Shell exit code is " + result);
        runShell.disconnect();
        return result == 0;

    }

	@Override
	public boolean autoDeploymentInstallation() {
		return runCommand(EnvironmentSetUpGetter.getautoDeploymentInstallationCommand(), 64800L);
	}
}
