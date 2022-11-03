package com.fobgochod.action;

import com.fobgochod.view.window.ZooToolWindow;
import com.fobgochod.ZKClient;
import com.intellij.openapi.actionSystem.AnAction;

public abstract class AbstractNodeAction extends AnAction {

    protected final ZooToolWindow zooToolWindow = ZooToolWindow.getInstance();
    protected final ZKClient zkClient = ZKClient.getInstance();
}
