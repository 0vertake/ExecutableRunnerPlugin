package com.milos.executablerunner

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import java.io.File

class ExecutableRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<ExecutableRunConfigurationOptions>(project, factory, name) {

    override fun getOptions(): ExecutableRunConfigurationOptions {
        return super.getOptions() as ExecutableRunConfigurationOptions
    }

    var executableType: ExecutableType
        get() = ExecutableType.fromName(options.executableType)
        set(value) { options.executableType = value.name }

    var customExecutablePath: String
        get() = options.customExecutablePath ?: ""
        set(value) { options.customExecutablePath = value }

    var arguments: String
        get() = options.arguments ?: ""
        set(value) { options.arguments = value }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return ExecutableRunConfigurationEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return ExecutableCommandLineState(this, environment)
    }

    override fun checkConfiguration() {
        super.checkConfiguration()

        when (executableType) {
            ExecutableType.CUSTOM -> {
                if (customExecutablePath.isBlank()) {
                    throw RuntimeConfigurationError("Please specify executable path")
                }
                if (!File(customExecutablePath).exists()) {
                    throw RuntimeConfigurationError("Executable not found: $customExecutablePath")
                }
            }
            else -> {
                if (!isCommandInPath(executableType.command!!)) {
                    throw RuntimeConfigurationError("${executableType.command} not found in PATH")
                }
            }
        }
    }

    private fun isCommandInPath(command: String): Boolean {
        val pathEnv = System.getenv("PATH") ?: return false
        val pathDirs = pathEnv.split(File.pathSeparator)

        val extensions = when {
            SystemInfo.isWindows -> listOf(".exe", ".cmd", ".bat")
            else -> listOf("")
        }

        for (dir in pathDirs) {
            for (ext in extensions) {
                if (File(dir, "$command$ext").exists()) {
                    return true
                }
            }
        }
        return false
    }
}
