<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# zookeeper-manager Changelog

## [Unreleased]

### Added

- Add charset settings

### Changed

- Use kotlin refactor settings
- Use zookeeper replace curator

### Removed

- Remove dependencies curator-framework
- Remove dependencies curator-client

## [2022.3.5] - 2024-01-10

### Fixed

- Fix chinese disorderly code #1

## [2022.3.4]

### Changed

- Adjust acl table column width
- Double click acl table row copy id
- Associate form when click acl item

### Fixed

- Fix non ttl node focus cannot be transferred.
- Fix NPE when node not selected.
- Fix expand tree when node not have read permission.

## [2022.3.3]

### Changed

- Update the plugin icon
- Define different icons according to the node type
- Update some actions icon and name
- Add deactivate tree action

## [2022.3.2]

### Changed

- Update `gradleVersion` to `7.6`
- Refactor create node view
- Refactor update ACLs info view

## [2022.3.1]

### Changed

- Update `platformVersion` to `2022.2.4`
- Change since/until build to `222-223.*` (2022.2 - 2022.3)
- Optimize tabs UI to IDEA style
- Add display type of data, such as json、html
- Refresh stat、acl in time after modifying relevant information
- Jump to the Log tab when an exception occurs

## [2022.3.0]

### Changed

- Update `platformVersion` to `2022.2.3`
- Change since/until build to `222-222.*` (2022.2 - 2022.2.3)
- Dependencies - upgrade `org.jetbrains.intellij` to `1.10.0`
- Add Container Nodes and TTL Nodes

## [2022.2.0]

### Changed

- Update `platformVersion` to `2022.2.3`
- Change since/until build to `222-222.*` (2022.2 - 2022.2.3)
- Dependencies - upgrade `org.jetbrains.kotlin.jvm` to `1.7.21`
- Dependencies - upgrade `org.jetbrains.intellij` to `1.9.0`
- Dependencies - upgrade `org.jetbrains.changelog` to `2.0.0`

## [2022.1.0]

### Changed

- Update `platformVersion` to `2022.1.4`
- Change since/until build to `221-221.*` (2022.1 - 2022.1.4)

## [2021.3.0]

### Changed

- Update `platformVersion` to `2021.3.3`
- Change since/until build to `213-213.*` (2021.3 - 2021.3.3)
- Dependencies - upgrade `org.jetbrains.kotlin.jvm` to `1.7.10`
- Dependencies - upgrade `org.jetbrains.intellij` to `1.8.0`
- Dependencies - upgrade `org.jetbrains.changelog` to `1.3.1`

## [2020.3.0]

### Added

- Update `platformVersion` to `2020.3.4`
- Change since/until build to `203-203.*` (2020.3 - 2020.3.4)
- Dependencies - upgrade `org.jetbrains.kotlin.jvm` to `1.5.30`
- Dependencies - upgrade `org.jetbrains.intellij` to `1.1.16`
- Dependencies - upgrade `org.jetbrains.changelog` to `1.3.0`

## [1.0.0]

### Added

- Initial project scaffold

[Unreleased]: https://github.com/fobgochod/zookeeper-manager/compare/v2022.3.5...HEAD

[2022.3.5]: https://github.com/fobgochod/zookeeper-manager/compare/v2022.3.4...v2022.3.5

[2022.3.4]: https://github.com/fobgochod/zookeeper-manager/compare/v2022.3.3...v2022.3.4

[2022.3.3]: https://github.com/fobgochod/zookeeper-manager/compare/v2022.3.2...v2022.3.3

[2022.3.2]: https://github.com/fobgochod/zookeeper-manager/compare/v2022.3.1...v2022.3.2

[2022.3.1]: https://github.com/fobgochod/zookeeper-manager/compare/v2022.3.0...v2022.3.1

[2022.3.0]: https://github.com/fobgochod/zookeeper-manager/compare/v2022.2.0...v2022.3.0

[2022.2.0]: https://github.com/fobgochod/zookeeper-manager/compare/v2022.1.0...v2022.2.0

[2022.1.0]: https://github.com/fobgochod/zookeeper-manager/compare/v2021.3.0...v2022.1.0

[2021.3.0]: https://github.com/fobgochod/zookeeper-manager/compare/v2020.3.0...v2021.3.0

[2020.3.0]: https://github.com/fobgochod/zookeeper-manager/compare/v1.0.0...v2020.3.0

[1.0.0]: https://github.com/fobgochod/zookeeper-manager/commits/v1.0.0
