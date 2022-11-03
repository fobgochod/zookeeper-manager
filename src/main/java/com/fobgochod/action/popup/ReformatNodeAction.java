package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.util.NoticeUtil;
import com.google.gson.*;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


/**
 * reformat node data
 *
 * @author Xiao
 * @date 2022/10/31 21:37
 */
public class ReformatNodeAction extends AbstractNodeAction {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void actionPerformed(@NotNull AnActionEvent event) {
        JTextArea text = zooToolWindow.getText();
        try {
            JsonElement je = JsonParser.parseString(text.getText());
            text.setText(gson.toJson(je));
        } catch (JsonSyntaxException e) {
            NoticeUtil.error("json parse exception: " + e.getMessage());
        }
    }
}
