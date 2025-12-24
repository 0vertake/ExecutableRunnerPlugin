package com.milos.executablerunner

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.util.execution.ParametersListUtil
import java.io.File

class ExecutableCommandLineState(
    private val configuration: ExecutableRunConfiguration,
    environment: ExecutionEnvironment
) : CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val executable = when (configuration.executableType) {
            ExecutableType.RUSTC -> "rustc"
            ExecutableType.CARGO -> "cargo"
            ExecutableType.CUSTOM -> configuration.customExecutablePath
        }

        val commandLine = GeneralCommandLine(executable)

        if (configuration.arguments.isNotBlank()) {
            val args = ParametersListUtil.parse(configuration.arguments)
            commandLine.addParameters(args)
        }

        environment.project.basePath?.let { basePath ->
            val workDir = File(basePath)
            if (workDir.exists() && workDir.isDirectory) {
                commandLine.workDirectory = workDir
            }
        }

        val processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)

        ProcessTerminatedListener.attach(processHandler)

        return processHandler
    }
}
