IntelliJ IDEA ZooKeeper Plugin
=======================================

<!-- Plugin description -->

ZooKeeper plugin for IntelliJ IDEA, and you can operate ZooKeeper directly in IDEA.

## Introduction

- This project is developed based on plugin [ZooKeeper](https://github.com/linux-china/zookeeper-intellij), because it
  is no longer maintained.
- The main purpose of this project is to learn about developing idea plugin, and learn about zookeeper by the way.
- If you have a bug or an idea, please create an issue at [GitHub](https://github.com/fobgochod/zookeeper-manager/issues).

## Features

* ZooKeeper's configuration for project: host, port
* ZeeKeeper tool window to display ZooKeeper file system tree
* Right click ZK tree node to popup operation menu
* Create Node: path support, such as org/apache/zookeeper
* Delete Node: will also delete children if they exist
* Update node value by popup menu
* Click ZK tree node to show data、stat、ACLs
* ......

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
