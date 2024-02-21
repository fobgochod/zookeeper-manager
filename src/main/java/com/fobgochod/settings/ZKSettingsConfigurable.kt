package com.fobgochod.settings

import com.fobgochod.ZKClient
import com.fobgochod.util.ZKBundle.message
import com.intellij.application.options.editor.CheckboxDescriptor
import com.intellij.application.options.editor.checkBox
import com.intellij.credentialStore.Credentials
import com.intellij.icons.AllIcons
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.observable.properties.ObservableMutableProperty
import com.intellij.openapi.observable.util.bind
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.messages.MessageDialog
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.dsl.builder.bindIntText
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign

/**
 *
 * @author fobgochod
 * @date 2024/2/21 22:38
 */
class ZKSettingsConfigurable : BoundSearchableConfigurable(
    message("plugin.name"), "zookeeper.manager"
) {
    private val state: ZKSettings get() = ZKSettings.instance

    private val saslClientEnabled get() = CheckboxDescriptor("Enable SASL authentication", state::saslClientEnabled)

    override fun createPanel(): DialogPanel {
        return panel {
            row {
                val hostListTable = HostListTable()
                cell(hostListTable.component).verticalAlign(VerticalAlign.FILL)
                    .onIsModified { hostListTable.isModified() }.onApply { hostListTable.apply() }
                    .onReset { hostListTable.reset() }

                panel {
                    group("Connection Settings") {

                        row("Name:") {
                            textField().bindText(state::name).resizableColumn().horizontalAlign(HorizontalAlign.FILL)
                        }

                        row("Host:") {
                            textField().bindText(state::host).resizableColumn().horizontalAlign(HorizontalAlign.FILL)

                            intTextField(0..65536)
                                .label("Port:")
                                .bindIntText(state::port)
                                .horizontalAlign(HorizontalAlign.RIGHT)
                        }

                        row("Path:") {
                            textField().bindText(state::path).horizontalAlign(HorizontalAlign.FILL)
                        }

                        row("BlockTimeout:") {
                            intTextField().bindIntText(state::blockUntilConnected)

                            button("Test Connection", object : AnAction() {
                                override fun actionPerformed(action: AnActionEvent) {
                                    val connectString =
                                        ZKSettings.instance.connectString(state.host, state.port, state.path)
                                    val zookeeper = ZKClient.getInstance().initZookeeper(
                                        connectString,
                                        state.blockUntilConnected,
                                        state.saslClientEnabled
                                    )
                                    if (zookeeper) {
                                        val title = "Connection to " + state.host
                                        val content = "Successfully connected!"
                                        val messageDialog = MessageDialog(
                                            content,
                                            title,
                                            arrayOf("OK"),
                                            0,
                                            AllIcons.General.BalloonInformation
                                        )
                                        messageDialog.setSize(300, 120)
                                        messageDialog.show()
                                    } else {
                                        val title = "Connection to " + state.host
                                        val content = "Cannot connect to remote host"
                                        val messageDialog = MessageDialog(
                                            content,
                                            title,
                                            arrayOf("OK"),
                                            0,
                                            AllIcons.General.BalloonError
                                        )
                                        messageDialog.setSize(300, 120)
                                        messageDialog.show()
                                    }
                                }
                            }).horizontalAlign(HorizontalAlign.RIGHT)
                        }
                    }

                    group("Other Settings") {
                        row {
                            textField()
                                .label("Charset:")
                                .bindText(state::charset)
                        }
                    }

                    collapsibleGroup("SASL Setting") {
                        row {
                            checkBox(saslClientEnabled)
                        }
                        row("Username:") {
                            textField().bindText(state::username)
                        }
                        row("Password:") {
                            cell(
                                // 密码另外处理
                                JBPasswordField()
                                    .columns(28)
                                    .bind(object : ObservableMutableProperty<String> {
                                        override fun set(value: String) {
                                            val credentials = Credentials("password", value)
                                            PasswordSafe.instance.set(
                                                ZKSettings.instance.credentialAttributes(),
                                                credentials
                                            )
                                        }

                                        override fun afterChange(listener: (String) -> Unit) {
                                        }

                                        override fun afterChange(
                                            listener: (String) -> Unit,
                                            parentDisposable: Disposable
                                        ) {
                                        }

                                        override fun get(): String {
                                            val pwd =
                                                PasswordSafe.instance.getPassword(ZKSettings.instance.credentialAttributes())
                                            return pwd ?: ""
                                        }
                                    })
                            )

//                            textField().bindText({
//                                val pwd = PasswordSafe.instance.getPassword(credentialAttributes())
//                                return@bindText pwd ?: ""
//                            }, {
//                                val credentials = Credentials("password", it)
//                                PasswordSafe.instance.set(credentialAttributes(), credentials)
//                            })
                        }
                    }
                }.horizontalAlign(HorizontalAlign.FILL)
                    .verticalAlign(VerticalAlign.FILL)
            }
        }
    }
}
