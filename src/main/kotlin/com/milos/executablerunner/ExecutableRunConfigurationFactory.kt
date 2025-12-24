package com.milos.executablerunner

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project

class ExecutableRunConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = ExecutableRunConfigurationType.ID

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return ExecutableRunConfiguration(project, this, "Executable")
    }

    override fun getOptionsClass(): Class<out BaseState> {
        return ExecutableRunConfigurationOptions::class.java
    }
}
