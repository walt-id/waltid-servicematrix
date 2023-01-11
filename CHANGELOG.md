# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]
-   Updated Kotlin plugin, Kotlin standard library and Kotlin reflection from 1.6.21 to 1.7.10
-   Updated hoplite-core and hoplite-hocon from 2.1.2 to 2.6.2
-   Updated kotest from 5.3.3 to 5.4.2
-   Fixed compilation with the walt.id SSI Kit
-   Updated error messages

## [1.1.2] - 2022-05-08

-   Upgraded to next major version of hoplite (1.4.16 -> 2.1.2)
-   bumped the rest of the dependencies


## [1.1.1] - 2022-05-08

-   Upgraded to next major version of hoplite (1.4.16 -> 2.1.2)
-   bumped the rest of the dependencies

## [1.1.0] - 2022-01-04

-   Added the possibility to define a default service implementation in the service _in-code_, without needing any
    configuration files (that you'd have to ship to customers when your software is used as dependency, that would need to
    be updated for new services, etc...)
-   More specific errors are thrown (e.g. by doing extra checks for class existence, checking base class validity, etc...)
-   More specific and helpful error messages
-   Bumped hoplite dependency

## [1.0.1] - 2021-07-20

Set kotlin option "jvmTarget" to 13

## [1.0.0] - 2021-07-20

### Added

-   Initial release

[Unreleased]: https://github.com/walt-id/waltid-servicematrix/compare/1.1.2...HEAD

[1.1.2]: https://github.com/walt-id/waltid-servicematrix/compare/1.1.1...1.1.2

[1.1.1]: https://github.com/walt-id/waltid-servicematrix/compare/1.1.1...1.1.1

[1.1.0]: https://github.com/walt-id/waltid-servicematrix/compare/1.0.1...1.1.0

[1.0.1]: https://github.com/walt-id/service-matrix/compare/1.0.0...1.0.1

[1.0.0]: https://github.com/walt-id/service-matrix/compare/1.0.0...1.0.0

[1.0.0]: https://github.com/walt-id/service-matrix/compare/acb37dcb0a1798686005e2e8e8b53420b522873e...1.0.0
