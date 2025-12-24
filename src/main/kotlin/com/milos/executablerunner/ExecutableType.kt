package com.milos.executablerunner

enum class ExecutableType(val displayName: String, val command: String?) {
    RUSTC("Rust Compiler (rustc)", "rustc"),
    CARGO("Cargo", "cargo"),
    CUSTOM("Custom Executable", null);

    override fun toString() = displayName

    companion object {
        fun fromName(name: String?): ExecutableType {
            return ExecutableType.entries.find { it.name == name } ?: RUSTC
        }
    }
}

