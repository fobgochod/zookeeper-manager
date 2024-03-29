package com.fobgochod.action.popup.tab.data;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.ZKBundle;
import com.google.gson.*;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * reformat node data
 *
 * @author fobgochod
 * @since 1.0
 */
public class ReformatDataAction extends AbstractNodeAction {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ReformatDataAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.reformat.data.text"));
        getTemplatePresentation().setIcon(AllIcons.Json.Object);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            JsonElement je = JsonParser.parseString(toolWindow.getData());
            toolWindow.setData(gson.toJson(je));
        } catch (JsonSyntaxException e) {
            NoticeUtil.error("json parse exception: " + e.getMessage());
        }
    }
}
