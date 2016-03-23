cmake_minimum_required (VERSION 3.5)
project (Runboard)

# Project directories
set(Project_Include_Dir  "${CMAKE_SOURCE_DIR}/include")
set(Project_Source_Dir   "${CMAKE_SOURCE_DIR}/src")

# Import wxWidgets
find_package(wxWidgets REQUIRED core base)
include(${wxWidgets_USE_FILE})

# Define include directories
include_directories(${Project_Include_Dir})

# Define executable
add_executable(Runboard
    ${Project_Source_Dir}/runboardapp.cpp
    ${Project_Source_Dir}/mainframe.cpp)
target_link_libraries(Runboard ${wxWidgets_LIBRARIES})
