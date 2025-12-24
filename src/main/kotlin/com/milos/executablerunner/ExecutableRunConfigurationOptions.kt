package com.milos.executablerunner

import com.intellij.execution.configurations.RunConfigurationOptions

class ExecutableRunConfigurationOptions : RunConfigurationOptions() {

    var executableType: String? by string(ExecutableType.RUSTC.name)
    var customExecutablePath: String? by string("")
    var arguments: String? by string("")
}
