package com.milos.executablerunner

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class ExecutableRunConfigurationEditor(private val project: Project) : SettingsEditor<ExecutableRunConfiguration>() {

    private val executableTypeCombo = ComboBox(ExecutableType.entries.toTypedArray())
    private val customPathField = TextFieldWithBrowseButton()
    private val argumentsField = JBTextField()

    init {
        customPathField.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFileOrExecutableAppDescriptor()
                .withTitle("Select Executable")
                .withDescription("Select the executable file to run")
        )

        executableTypeCombo.addActionListener {
            updateCustomPathFieldState()
        }

        updateCustomPathFieldState()
    }

    private fun updateCustomPathFieldState() {
        customPathField.isEnabled = executableTypeCombo.selectedItem == ExecutableType.CUSTOM
    }

    override fun resetEditorFrom(config: ExecutableRunConfiguration) {
        executableTypeCombo.selectedItem = config.executableType
        customPathField.text = config.customExecutablePath
        argumentsField.text = config.arguments
        updateCustomPathFieldState()
    }

    override fun applyEditorTo(config: ExecutableRunConfiguration) {
        config.executableType = executableTypeCombo.selectedItem as ExecutableType
        config.customExecutablePath = customPathField.text
        config.arguments = argumentsField.text
    }

    override fun createEditor(): JComponent {
        return panel {
            row("Executable:") {
                cell(executableTypeCombo)
                    .columns(COLUMNS_MEDIUM)
                    .comment("Select a predefined executable or choose 'Custom Executable'")
            }

            row("Executable path:") {
                cell(customPathField)
                    .align(AlignX.FILL)
                    .comment("Path to the executable file (only for Custom Executable)")
            }

            row("Arguments:") {
                cell(argumentsField)
                    .align(AlignX.FILL)
                    .comment("Command line arguments to pass to the executable")
            }
        }
    }
}
