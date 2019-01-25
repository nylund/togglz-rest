# A REST repository for Togglz

A simple implementation of a REST API and REST repository for
Togglz implemented using Spring Boot 2.x.

The purpose of this example is to create a feature flag architecture
for multiple micro services where a single service, the feature flag
service, holds the state of all feature flags and individual micro
services use the REST API of the feature flag service to create a local
in-memory feature flag repository.

### Usage

This is a proof of concept where the code base includes both the
feature flag service and a dummy micro service accessing the shared
feature flag service.

#### The feature flag service aka master
  - Activated with `master` Spring profile
  - This exposes a read only REST API for to a JDBC backed Togglz
    repository
  - Service runs on port `8080` and exposes
    `http://localhost:8080/features-api/v1/state` used by clients
  - Togglz management console is exposed on `http://localhost:8080/feature-flags/index`

#### Example micro service aka client
  - Activated with `client` Spring profile
  - This injects a cached RESTStateReposity that reads feature flags
    using the master REST API
  - Service runs on port `8081`

Note, no authentication has been implemented between master and client.

