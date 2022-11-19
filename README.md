IntelliJ IDEA ZooKeeper Plugin
=======================================

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
