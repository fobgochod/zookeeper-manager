package com.fobgochod.constant;

import com.intellij.ui.JBColor;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public enum ZKStyle {

    ERROR {
        @Override
        protected void init() {
            super.init();
            StyleConstants.setForeground(style, JBColor.RED);
        }
    },
    WARN {
        @Override
        protected void init() {
            super.init();
            StyleConstants.setForeground(style, JBColor.MAGENTA);
        }
    },
    INFO {
        @Override
        protected void init() {
            super.init();
        }
    },
    DEBUG {
        @Override
        protected void init() {
            super.init();
            StyleConstants.setForeground(style, JBColor.GRAY);
        }
    };

    static {
        for (ZKStyle style : values()) {
            style.init();
        }
    }

    protected MutableAttributeSet style;

    protected void init() {
        style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, JBColor.foreground());
    }

    public MutableAttributeSet get() {
        return style;
    }
}
