# IntelliJ IDEA ZooKeeper Plugin

[![Build](https://github.com/fobgochod/zookeeper-manager/workflows/Build/badge.svg)](https://github.com/fobgochod/zookeeper-manager/actions?query=workflow%3ABuild)
[![Version](https://img.shields.io/jetbrains/plugin/v/20317.svg)](https://plugins.jetbrains.com/plugin/20317)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/20317.svg)](https://plugins.jetbrains.com/plugin/20317)


<!-- Plugin description -->

Provides integration with [Zookeeper](https://zookeeper.apache.org).

## Introduction

- This project is developed based on plugin [ZooKeeper](https://github.com/linux-china/zookeeper-intellij), because it
  is no longer maintained.
- If you have a bug or an idea, please create an issue
  at [GitHub](https://github.com/fobgochod/zookeeper-manager/issues).

## Features

* To configure, open Settings | Tools | Zookeeper.
* Click "Refresh" to display ZooKeeper file system tree
* Right click tree node to popup operation menu
    * Create Node: path support, such as org/apache/zookeeper
    * Delete Node: will also delete children if they exist
    * Config Node: allow to config ACL if you have permissions
* Click tree node to show data、stat、ACLs
    * Data Tab allow update node value by popup menu
    * Stat Tab display node stat structure
    * ACL Tab show node ACL info
    * Log Tab print some operation log

To use the **Zookeeper** tool window, select **View** > **Tool Windows** > **Zookeeper**.

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  zookeeper-manager"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/fobgochod/zookeeper-manager/releases/latest) and install it manually
  using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

## Reference

- Zookeeper source material
    - https://svn.apache.org/repos/asf/zookeeper/logo
- Zookeeper SVG:
    - https://github.com/cncf/landscape/issues/396
    - https://github.com/cncf/landscape/issues/271
- Platform UI Guidelines:
    - [Icons list](https://jetbrains.design/intellij/resources/icons_list)
    - [Icons](https://jetbrains.design/intellij/principles/icons)

## IntelliJ Icon Generator

- [IntelliJ Icon Generator](https://bjansen.github.io/intellij-icon-generator)

| settings       | persistent | ephemeral | persistent_sequential | ephemeral_sequential | container | persistent_with_ttl | persistent_sequential_with_ttl |
|:---------------|------------|-----------|-----------------------|----------------------|-----------|---------------------|--------------------------------|
| Shape          | Circle     | Circle    | Circle                | Circle               | Circle    | Circle              | Circle                         |
| Radius         | 7          | 7         | 7                     | 7                    | 7         | 7                   | 7                              |
| Bg.color       | #6D9758    | #6D9758   | #6D9758               | #6D9758              | #6D9758   | #6D9758             | #6D9758                        |
| Bg.opacity     | 60         | 60        | 60                    | 60                   | 60        | 60                  | 60                             |
| Stroke         | 0          | 0         | 0                     | 0                    | 0         | 0                   | 0                              |
| Stroke.Color   |            |           |                       |                      |           |                     |                                |
| Stroke.opacity |            |           |                       |                      |           |                     |                                |
| Font           | Menlo      | Menlo     | Menlo                 | Menlo                | Menlo     | Menlo               | Menlo                          |
| Text           | P          | E         | Ps                    | Es                   | C         | T                   | Ts                             |
| Text.Color     | #4F2E0E    | #4F2E0E   | #4F2E0E               | #4F2E0E              | #4F2E0E   | #4F2E0E             | #4F2E0E                        |
| Text.opacity   | 70         | 70        | 70                    | 70                   | 70        | 70                  | 70                             |
| X.offset       | 35         | 35        | 35                    | 35                   | 35        | 35                  | 35                             |
| Y.offset       | 25         | 25        | 45                    | 45                   | 25        | 25                  | 45                             |
| Scale          | 77         | 77        | 60                    | 60                   | 77        | 77                  | 60                             |
