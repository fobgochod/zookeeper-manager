package com.fobgochod.view.editor;

import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.ZKBundle;
import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.ui.EditorTextField;
import com.intellij.util.LocalTimeCounter;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.border.Border;

/**
 * data editor
 *
 * @author fobgochod
 * @since 1.0
 */
public class ZKNodeEditor extends EditorTextField {

    /**
     * 文本格式
     */
    public static final FileType TEXT_FILE_TYPE = FileTypes.PLAIN_TEXT;
    /**
     * json格式
     */
    public static final FileType JSON_FILE_TYPE = FileTypeManager.getInstance().getStdFileType("JSON");
    /**
     * html格式
     */
    public static final FileType HTML_FILE_TYPE = HtmlFileType.INSTANCE;
    /**
     * xml格式
     */
    public static final FileType XML_FILE_TYPE = XmlFileType.INSTANCE;

    public ZKNodeEditor(Project project) {
        this(project, TEXT_FILE_TYPE);
    }

    public ZKNodeEditor(Project project, FileType fileType) {
        super(null, project, fileType, false, false);
    }

    public static void setupCustomTextFieldEditor(@NotNull EditorEx editor) {
        EditorSettings settings = editor.getSettings();
        settings.setFoldingOutlineShown(true);
        settings.setLineNumbersShown(true);
        settings.setIndentGuidesShown(true);
        editor.setHorizontalScrollbarVisible(true);
        editor.setVerticalScrollbarVisible(true);
    }

    public void setText(@Nullable final String text, @NotNull final FileType fileType) {
        super.setFileType(fileType);
        Document document = createDocument(text, fileType);
        setDocument(document);
        PsiFile psiFile = PsiDocumentManager.getInstance(getProject()).getPsiFile(document);
        if (psiFile != null) {
            try {
                WriteCommandAction.runWriteCommandAction(
                        getProject(),
                        () -> {
                            CodeStyleManager.getInstance(getProject()).reformat(psiFile);
                        }
                );
            } catch (Exception e) {
                NoticeUtil.error(e.getMessage());
            }
        }
    }

    @Override
    public void setFileType(@NotNull FileType fileType) {
        setNewDocumentAndFileType(fileType, createDocument(getText(), fileType));
    }

    @Override
    protected Document createDocument() {
        return createDocument(null, getFileType());
    }

    private void initOneLineModePre(@NotNull final EditorEx editor) {
        editor.setOneLineMode(false);
        editor.setColorsScheme(editor.createBoundColorSchemeDelegate(null));
        editor.getSettings().setCaretRowShown(false);
    }

    @NotNull
    @Override
    protected EditorEx createEditor() {
        EditorEx editor = super.createEditor();
        initOneLineModePre(editor);
        setupCustomTextFieldEditor(editor);
        return editor;
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        super.repaint(tm, x, y, width, height);
        if (getEditor() instanceof EditorEx) {
            initOneLineModePre(((EditorEx) getEditor()));
        }
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(JBUI.Borders.empty());
    }

    public Document createDocument(@Nullable final String text, @NotNull final FileType fileType) {
        final PsiFileFactory factory = PsiFileFactory.getInstance(getProject());
        final long stamp = LocalTimeCounter.currentTime();
        final PsiFile psiFile = factory.createFileFromText(
                ZKBundle.message("plugin.name"),
                fileType,
                text == null ? "" : text,
                stamp,
                true,
                false
        );
        return PsiDocumentManager.getInstance(getProject()).getDocument(psiFile);
    }
}
