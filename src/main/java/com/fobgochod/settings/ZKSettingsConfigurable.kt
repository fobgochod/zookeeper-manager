package com.fobgochod.settings

import com.fobgochod.ZKClient
import com.fobgochod.util.ZKBundle.message
import com.intellij.application.options.editor.CheckboxDescriptor
import com.intellij.application.options.editor.checkBox
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.messages.MessageDialog
import com.intellij.openapi.ui.setEmptyState
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.selected

/**
 * Zookeeper Settings Configurable
 *
 * @author fobgochod
 * @since 1.0
 */
class ZKSettingsConfigurable : BoundSearchableConfigurable(
    message("configurable.display.name"), "zookeeper.manager"
) {
    private val state: ZKSettings get() = ZKSettings.instance

    private val enableSasl get() = CheckboxDescriptor(message("settings.sasl.enable"), state::enableSasl)

    override fun createPanel(): DialogPanel {
        lateinit var hostModel: JBTextField
        lateinit var portModel: JBTextField
        lateinit var pathModel: JBTextField
        lateinit var sessionTimeoutModel: JBTextField
        lateinit var enableSaslModel: JBCheckBox

        return panel {
            group(message("settings.group.connection")) {
                row(message("settings.connection.name")) {
                    textField().align(Align.FILL).bindText(state::name)
                }

                row(message("settings.connection.host")) {
                    hostModel = textField()
                        .applyToComponent { setEmptyState("172.16.2.1,172.16.2.2,172.16.2.3") }
                        .resizableColumn()
                        .align(Align.FILL)
                        .bindText(state::host).component

                    portModel = intTextField(0..65536)
                        .label(message("settings.connection.port"))
                        .columns(10)
                        .align(AlignX.RIGHT)
                        .bindIntText(state::port).component
                }

                row(message("settings.connection.path")) {
                    pathModel = textField()
                        .applyToComponent { setEmptyState("/zookeeper") }
                        .align(Align.FILL)
                        .bindText(state::path).component
                }

                row(message("settings.connection.session.timeout")) {
                    sessionTimeoutModel = intTextField()
                        .columns(10)
                        .bindIntText(state::sessionTimeout).component

                    button(
                        message("settings.connection.test"), object : AnAction() {
                            // @formatter:off
                            override fun actionPerformed(action: AnActionEvent) {
                                val connectString = ZKSettings.instance.connectString(hostModel.text, portModel.text.toInt(), pathModel.text)
                                val zookeeper = ZKClient.getInstance().init(connectString, sessionTimeoutModel.text.toInt(), enableSaslModel.isSelected)
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
                    ).align(AlignX.RIGHT)
                }
            }

            group(message("settings.group.other")) {
                row(message("settings.other.charset")) {
                    textField().bindText(state::charset)
                }

                row(message("settings.other.admin.server")) {
                    textField()
                        .align(Align.FILL)
                        .bindText(state::adminServer)
                }.rowComment(
                    "Config admin.enableServer=false to disable the <a href='https://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_adminserver'>AdminServer</a>.",
                    MAX_LINE_LENGTH_WORD_WRAP
                )
            }

            group(message("settings.group.sasl")) {
                row {
                    enableSaslModel = checkBox(enableSasl).component
                }

                indent {
                    row(message("settings.sasl.username")) {
                        textField()
                            .align(Align.FILL)
                            .bindText(state::username)
                    }
                    row(message("settings.sasl.password")) {
                        val pwdField = JBPasswordField()
                        cell(pwdField).align(Align.FILL)
                        loadPasswordAsync { pwd ->
                            pwdField.text = pwd
                        }
                    }
                }.enabledIf(enableSaslModel.selected)
            }
        }
    }

    fun loadPasswordAsync(onResult: (String) -> Unit) {
        ApplicationManager.getApplication().executeOnPooledThread {
            val pwd = state.password
            ApplicationManager.getApplication().invokeLater {
                onResult(pwd)
            }
        }
    }

}
