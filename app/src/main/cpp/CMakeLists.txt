# CMakeLists.txt - Configuración para compilar código nativo C
cmake_minimum_required(VERSION 3.22.1)
project("nextmatch_gps")

# Agregar la librería nativa
add_library(nextmatch_gps SHARED
        gps_calculator.c)

# Vincular con la librería log de Android
find_library(log-lib log)
target_link_libraries(nextmatch_gps ${log-lib})