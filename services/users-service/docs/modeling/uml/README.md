# UML Diagrams - Medical Services Network

## 📁 Structure

```
docs/modeling/uml/
├── main-packages.puml                         # Package diagram
├── domain/                                    # Domain Layer
│   ├── 01-entities.puml                       # Entities relations
│   ├── 02-dtos.puml                           # DTOs relations
│   ├── 03-factory.puml                        # Factory relations
│   ├── 04-factory-operations.puml             # Factory operations
│   ├── 05-observer-pattern.puml               # Observer relations
│   └── 06-observer-pattern-operations.puml    # Observer operations
├── application/                               # Application Layer
│   ├── 01-services.puml                       # Services relations
│   └── 02-services-operations.puml            # Services operations
├── infrastructure/                            # Infrastructure Layer
│   ├── 01-repositories.puml                   # Repositories relations
│   └── 02-repositories-operations.puml        # Repositories operations
├── presentation/                              # Presentation Layer
│   ├── 01-views.puml                         # Views relations
│   └── 02-views-operations.puml               # Views operations
└── sequences/                                 # Sequence Diagrams
    ├── 01-create-user.puml                    # Create user flow
    ├── 02-strategy-pattern.puml                # Strategy pattern flow
    └── 03-observer-pattern.puml                # Observer pattern flow
```

## 📋 Diagram Types

### Relations Diagrams (01-*.puml)
- Show class structure and **relationships** between classes
- Include cardinalities and relationship names
- NO operations/methods shown

### Operations Diagrams (02-*-operations.puml)
- Show **operations/methods** and attributes
- NO relationships between classes

## 🎨 Color Conventions

| Color | Meaning |
|-------|---------|
| **SkyBlue** | Views (UI Components) |
| **PaleGreen** | Services, Repositories, Factories |
| **LightYellow** | Entities |
| **LightCyan** | DTOs |
| **aliceblue** | Interfaces |
| **LightBlue** | Enums |

## 🔗 Layer Dependencies

```
presentation → application → domain ← infrastructure
```

## 🔍 How to Visualize

### Option 1: VS Code (PlantUML Extension)
1. Install "PlantUML" extension
2. Open .puml file
3. Cmd+Shift+P → "PlantUML: Preview"

### Option 2: Online
- https://www.plantuml.com/plantuml/

### Option 3: Docker
```bash
docker run -d -p 8080:8080 plantuml/plantuml-server
```