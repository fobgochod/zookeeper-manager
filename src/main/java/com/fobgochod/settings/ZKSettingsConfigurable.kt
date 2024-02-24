package com.fobgochod.settings

import com.fobgochod.ZKClient
import com.fobgochod.util.ZKBundle.message
import com.intellij.application.options.editor.CheckboxDescriptor
import com.intellij.application.options.editor.checkBox
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.observable.util.bind
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.messages.MessageDialog
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.bindIntText
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign

/**
 * Zookeeper Settings Configurable
 *
 * @author fobgochod
 * @date 2024/2/21 22:38
 */
class ZKSettingsConfigurable : BoundSearchableConfigurable(
    message("configurable.display.name"), "zookeeper.manager"
) {
    private val state: ZKSettings get() = ZKSettings.instance

    private val saslClientEnabled get() = CheckboxDescriptor(message("settings.sasl.enable"), state::saslClientEnabled)

    override fun createPanel(): DialogPanel {
        lateinit var hostModel: JBTextField
        lateinit var portModel: JBTextField
        lateinit var pathModel: JBTextField
        lateinit var blockUntilConnectedModel: JBTextField
        lateinit var saslClientEnabledModel: JBCheckBox

        return panel {
            group(message("settings.group.connection")) {
                row(message("settings.connection.name")) {
                    textField().resizableColumn().horizontalAlign(HorizontalAlign.FILL).bindText(state::name)
                }

                row(message("settings.connection.host")) {
                    hostModel = textField().resizableColumn().horizontalAlign(HorizontalAlign.FILL)
                        .bindText(state::host).component

                    portModel = intTextField(0..65536).label("Port:")
                        .columns(10)
                        .horizontalAlign(HorizontalAlign.RIGHT)
                        .bindIntText(state::port).component
                }

                row(message("settings.connection.path")) {
                    pathModel = textField().horizontalAlign(HorizontalAlign.FILL)
                        .bindText(state::path).component
                }

                row(message("settings.connection.block.timeout")) {
                    blockUntilConnectedModel = intTextField()
                        .columns(10)
                        .bindIntText(state::blockUntilConnected).component

                    button(
                        message("settings.connection.test"),
                        object : AnAction() {
                            // @formatter:off
                                    override fun actionPerformed(action: AnActionEvent) {
                                        val connectString = ZKSettings.instance.connectString(hostModel.text, portModel.text.toInt(), pathModel.text)
                                        val zookeeper = ZKClient.getInstance().init(connectString, blockUntilConnectedModel.text.toInt(), saslClientEnabledModel.isSelected)
                                        if (zookeeper) {
                                            val title = "Connection to " + hostModel.text
                                            val content = "Successfully connected!"
                                            val messageDialog = MessageDialog(content, title, arrayOf("OK"), 0, AllIcons.General.BalloonInformation)
                                            messageDialog.setSize(300, 120)
                                            messageDialog.show()
                                        } else {
                                            val title = "Connection to " + hostModel.text
                                            val content = "Cannot connect to remote host"
                                            val messageDialog = MessageDialog(content, title, arrayOf("OK"), 0, AllIcons.General.BalloonError)
                                            messageDialog.setSize(300, 120)
                                            messageDialog.show()
                                        }
                                    }
                                    // @formatter:on
                        }
                    ).horizontalAlign(HorizontalAlign.RIGHT)
                }
            }

            group(message("settings.group.other")) {
                row(message("settings.other.charset")) {
                    textField().bindText(state::charset)
                }
            }

            group(message("settings.group.sasl")) {
                row {
                    saslClientEnabledModel = checkBox(saslClientEnabled).component
                }
                row(message("settings.sasl.username")) {
                    textField().bindText(state::username)
                }
                row(message("settings.sasl.password")) {
                    cell(JBPasswordField().columns(28).bind(PasswordProperty()))
                }
            }
        }
    }
}
