package com.fobgochod.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

public class SingleUtil {

    public static Project getProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects.length > 0) {
            return projects[0];
        }
        return null;
    }
}
