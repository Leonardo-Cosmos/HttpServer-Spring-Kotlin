rootProject.name = "spring-http-service"

include("app.http")
include("sample.http")

project(":app.http").projectDir = file("projects/app-http")
project(":sample.http").projectDir = file("projects/business/sample-http")

