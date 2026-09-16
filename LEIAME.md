# Visão Noturna (Fabric 26.3, client-side)

Aperte **N** para ligar/desligar. A tecla pode ser trocada em
Opções > Controles > Atalhos de teclado > "Visão Noturna".

## Compilar
Requer **JDK 25**.

    ./gradlew build        (Linux/macOS)
    gradlew.bat build      (Windows)

O mod sai em `build/libs/visao-noturna-1.0.0.jar`.
Coloque-o na pasta `mods` junto com o **Fabric API** para 26.3.

## Testar no ambiente de desenvolvimento
    ./gradlew runClient
