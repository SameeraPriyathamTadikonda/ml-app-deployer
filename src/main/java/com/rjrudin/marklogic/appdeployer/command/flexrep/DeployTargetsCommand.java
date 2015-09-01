package com.rjrudin.marklogic.appdeployer.command.flexrep;

import java.io.File;

import com.rjrudin.marklogic.appdeployer.command.AbstractCommand;
import com.rjrudin.marklogic.appdeployer.command.CommandContext;
import com.rjrudin.marklogic.mgmt.flexrep.TargetManager;

/**
 * The directory structure for this is a bit different from most command. Since targets belong to a certain flexrep
 * config, this command looks for every directory under flexrep/configs that ends with "-targets". For each such
 * directory, the flexrep config name is determined by stripping "-targets" off the directory name, and then each target
 * JSON/XML file in the directory is loaded for that flexrep config name.
 */
public class DeployTargetsCommand extends AbstractCommand {

    private String databaseIdOrName;
    private String targetDirectorySuffix = "-targets";

    @Override
    public void execute(CommandContext context) {
        File configDir = new File(context.getAppConfig().getConfigDir().getFlexrepDir(), "configs");
        for (File f : configDir.listFiles()) {
            if (f.isDirectory() && f.getName().endsWith(targetDirectorySuffix)) {
                deployTargets(f, context);
            }
        }
    }

    protected void deployTargets(File dir, CommandContext context) {
        String dbName = databaseIdOrName != null ? databaseIdOrName : context.getAppConfig().getContentDatabaseName();
        String name = dir.getName();
        String configName = name.substring(0, name.length() - targetDirectorySuffix.length());

        if (logger.isInfoEnabled()) {
            logger.info(format("Deploying flexrep targets with config name '%s' in directory: %s", configName,
                    dir.getAbsolutePath()));
        }

        TargetManager mgr = new TargetManager(context.getManageClient(), dbName, configName);
        for (File f : listFilesInDirectory(dir)) {
            saveResource(mgr, context, f);
        }

    }

    public void setDatabaseIdOrName(String databaseIdOrName) {
        this.databaseIdOrName = databaseIdOrName;
    }

    public void setTargetDirectorySuffix(String targetDirectorySuffix) {
        this.targetDirectorySuffix = targetDirectorySuffix;
    }

}