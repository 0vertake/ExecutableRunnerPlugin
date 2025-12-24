package com.milos.executablerunner

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.NotNullLazyValue

class ExecutableRunConfigurationType : ConfigurationTypeBase(
    ID,
    "Executable Runner",
    "Run custom executables with arguments",
    NotNullLazyValue.createValue { AllIcons.RunConfigurations.Application }
) {
    companion object {
        const val ID = "ExecutableRunConfiguration"
    }

    init {
        addFactory(ExecutableRunConfigurationFactory(this))
    }
}
