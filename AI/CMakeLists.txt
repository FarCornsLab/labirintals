cmake_minimum_required(VERSION 3.17)
project(AI)

set(CMAKE_CXX_STANDARD 20)

include(FetchContent)

FetchContent_Declare(
        asio
        GIT_REPOSITORY https://github.com/chriskohlhoff/asio
        GIT_TAG asio-1-14-0
        UPDATE_DISCONNECTED 1
)
SET(CMAKE_CXX_FLAGS "-DASIO_STANDALONE -pthread")
FetchContent_Populate(asio)
include_directories(${asio_SOURCE_DIR}/asio/include/)

if(WIN32)
    link_libraries(ws2_32 wsock32)
endif()

FetchContent_Declare(
        json
        GIT_REPOSITORY https://github.com/nlohmann/json.git
        GIT_TAG v3.7.3)
FetchContent_GetProperties(json)
if(NOT json_POPULATED)
    FetchContent_Populate(json)
    add_subdirectory(${json_SOURCE_DIR} ${json_BINARY_DIR} EXCLUDE_FROM_ALL)
endif()

set(HEADERS
        Algorithms/ExecutorAlgorithm.h
        Algorithms/LucaTremoAlgorithm.h
        Client/Client.h
        Client/Commands.h
        Fields/Field.h
        Fields/FieldDescription.h
        Fields/ServerField.h
        Manager/Executor/ManagerExecutor.h
        Manager/Executor/ServerManager.h
        Manager/Visualization/LogManagerVisualization.h
        Manager/Visualization/ManagerVisualization.h
        Manager/Manager.h
        Navigation/AttendanceMap.h
        Navigation/BorderMap.h
        Navigation/Map.h
        Navigation/Position.h
        Bot.h
        )

set(DEFINITIONS
        Algorithms/LucaTremoAlgorithm.cpp
        Client/Client.cpp
        Fields/ServerField.cpp
        Manager/Executor/ServerManager.cpp
        Manager/Visualization/LogManagerVisualization.cpp
        Navigation/BorderMap.cpp
        Navigation/Map.cpp
        Navigation/Position.cpp
        Bot.cpp
        Client/Commands.cpp)

include_directories(
        Algorithms
        Client
        Fields
        Manager
        Navigation
        )

add_executable(${PROJECT_NAME} main.cpp ${HEADERS} ${DEFINITIONS})
target_link_libraries(${PROJECT_NAME} PRIVATE nlohmann_json::nlohmann_json)
