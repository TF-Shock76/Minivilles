@echo off

cls

set /p choix="Que voulez vous lancer ? (c = compilation, e = execution, a = les deux, q = quitter)"

if "%choix%"=="c" (
    echo Execution des compilations
    echo.

    javac -encoding utf-8 @compile.list -d "./"

    echo.
    echo terminer
    set /p test="Appuyer pour quitter"
)

if "%choix%"=="a" (
    echo Execution des compilations
    echo.

    javac -encoding utf-8 @compile.list -d "./"

    echo.
    echo terminer
)

if "%choix%"=="d" (
    echo Execution des compilations
    echo.

    javac -encoding utf-8 @compile.list -d "./"

    echo.
    echo terminer
)

if "%choix%"=="e" (

    java execution.Controleur
    set /p test="Appuyer pour quitter"
)

if "%choix%"=="a" (

    java execution.Controleur
    set /p test="Appuyer pour quitter"
)

if "%choix%"=="d" (

    java execution.Controleur debug
    set /p test="Appuyer pour quitter"
)
