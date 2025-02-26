rootProject.name = "spring-http-service"

include("app.http")
include("app.kafka")
include("common.kafka")
include("sample.http")
include("sample.kafka")

project(":app.http").projectDir = file("projects/app-http")
project(":app.kafka").projectDir = file("projects/app-kafka")
project(":common.kafka").projectDir = file("projects/common/common-kafka")
project(":sample.http").projectDir = file("projects/business/sample-http")
project(":sample.kafka").projectDir = file("projects/business/sample-kafka")
