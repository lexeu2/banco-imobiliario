# 🏦 Banco Imobiliário - JavaFX Game

## 📋 Pré-requisitos

| Ferramenta | Versão | Download |
|-----------|--------|----------|
| **JDK** | 17+ | https://adoptium.net/download/ |
| **Maven** | 3.9+ | https://maven.apache.org/download.cgi |

---

## 🚀 Como Rodar

### 1. Clonar o projeto

```cmd
git clone https://github.com/lexeu2/banco-imobiliario.git
cd banco-imobiliario

2. Executar
cmd

mvn clean javafx:run

    Na primeira vez, o Maven vai baixar as dependências automaticamente (~1-2 minutos).

🎮 Como Jogar
Ação	Botão
Novo jogo	Menu Game → New Game
Jogar dado	Roll Dice (2D6)
Comprar terreno	Buy Property
Passar vez	End Turn
📁 Estrutura do Projeto
text

src/main/java/com/monopoly/
├── Main.java              # Inicia o jogo
├── model/                 # Game, Player, Property
├── facade/                # GameFacade (simplifica operações)
├── singleton/             # DatabaseManager, GameEventManager
├── factory/               # PropertyFactory, BoardSpaceFactory
├── decorator/             # PropertyDecorator, MonopolyDecorator
├── observer/              # GameObserver, Logger
├── strategy/              # PlayerStrategy (AI)
├── ui/                    # GameUI, BoardPanel
└── database/              # GameRepository

🎨 Design Patterns
Pattern	Onde
Singleton	DatabaseManager, GameEventManager, GameFacade
Factory Method	BoardSpaceFactory, PropertyFactory
Facade	GameFacade
Decorator	PropertyDecorator, MonopolyDecorator
Observer	GameObserver, Logger, AchievementSystem
Strategy	PlayerStrategy (Aggressive, Conservative, Balanced)
🗄️ Banco de Dados (Opcional)

O jogo funciona sem MySQL. Para salvar jogos:

    Instalar MySQL

    Criar banco: CREATE DATABASE monopoly_db;

    Editar src/main/resources/META-INF/persistence.xml com sua senha

🛠️ Tecnologias

    Java 17

    JavaFX

    Hibernate/JPA

    MySQL

    Maven
