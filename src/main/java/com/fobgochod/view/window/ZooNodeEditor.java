package com.fobgochod.view.window;

import com.intellij.ide.highlighter.HtmlFileHighlighter;
import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.ide.highlighter.XmlFileHighlighter;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.json.JsonFileType;
import com.intellij.json.highlighting.JsonSyntaxHighlighterFactory;
import com.intellij.lang.Language;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.*;
import com.intellij.psi.tree.IElementType;

public class ZooNodeEditor {

    private static final IElementType TEXT_ELEMENT_TYPE = new IElementType("TEXT", Language.ANY);
    private static volatile ZooNodeEditor instance;
    private EditorEx editor;

    private ZooNodeEditor() {
        createEditor();
    }

    public static ZooNodeEditor getInstance() {
        if (instance == null) {
            synchronized (ZooNodeEditor.class) {
                if (instance == null) {
                    instance = new ZooNodeEditor();
                }
            }
        }
        return instance;
    }

    public Editor getEditor() {
        return editor;
    }

    public void showPretty(String jsonString) {
        WriteAction.run(() -> {
            Document document = editor.getDocument();
            document.setReadOnly(false);
            document.setText(jsonString);
            document.setReadOnly(true);
        });
        editor.setHighlighter(getHighlighter(JsonFileType.INSTANCE));
    }

    private void createEditor() {
        EditorFactory editorFactory = EditorFactory.getInstance();
        Document document = editorFactory.createDocument("");
        editor = (EditorEx) editorFactory.createEditor(document);
        EditorSettings editorSettings = editor.getSettings();
        editorSettings.setVirtualSpace(false);
        editorSettings.setLineMarkerAreaShown(false);
        editorSettings.setIndentGuidesShown(false);
        editorSettings.setFoldingOutlineShown(true);
        editorSettings.setAdditionalColumnsCount(3);
        editorSettings.setAdditionalLinesCount(3);
        editorSettings.setLineNumbersShown(true);
        editorSettings.setCaretRowShown(true);
        editor.setHighlighter(getHighlighter(FileTypes.PLAIN_TEXT));
    }

    private EditorHighlighter getHighlighter(LanguageFileType fileType) {
        SyntaxHighlighter highlighter = getFileHighlighter(fileType);
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        LayeredLexerEditorHighlighter lexerHighlighter = new LayeredLexerEditorHighlighter(highlighter, scheme);
        lexerHighlighter.registerLayer(TEXT_ELEMENT_TYPE, new LayerDescriptor(highlighter, ""));
        return lexerHighlighter;
    }

    private SyntaxHighlighter getFileHighlighter(FileType fileType) {
        if (fileType == HtmlFileType.INSTANCE) {
            return new HtmlFileHighlighter();
        } else if (fileType == XmlFileType.INSTANCE) {
            return new XmlFileHighlighter();
        } else if (fileType == JsonFileType.INSTANCE) {
            return JsonSyntaxHighlighterFactory.getSyntaxHighlighter(fileType, null, null);
        }
        return new PlainSyntaxHighlighter();
    }
}
