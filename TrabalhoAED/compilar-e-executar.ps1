# Compila e executa o simulador. Requer um JDK (javac) no PATH.
# Uso: clique direito > "Executar com PowerShell", ou no terminal: .\compilar-e-executar.ps1

$ErrorActionPreference = 'Stop'
$raiz = Split-Path -Parent $MyInvocation.MyCommand.Path
$src  = Join-Path $raiz 'src'
$bin  = Join-Path $raiz 'bin'

if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
    Write-Host "ERRO: 'javac' nao encontrado. Instale um JDK (ex.: Temurin/OpenJDK 17) e adicione ao PATH." -ForegroundColor Red
    exit 1
}

if (-not (Test-Path $bin)) { New-Item -ItemType Directory -Path $bin | Out-Null }

# Lista todos os arquivos .java do projeto.
$fontes = Get-ChildItem -Path $src -Recurse -Filter *.java | ForEach-Object { $_.FullName }

Write-Host "Compilando..." -ForegroundColor Cyan
javac -encoding UTF-8 -d $bin $fontes

Write-Host "Executando (aed.Main)...`n" -ForegroundColor Cyan
java -cp $bin aed.Main
