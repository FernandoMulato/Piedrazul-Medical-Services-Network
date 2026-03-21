# C4 Model

## Context 

@startuml Context
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml

title Sistema de Agendamiento de Citas Médicas - Nivel 1 (Contexto)

Person(paciente, "Paciente", "Agenda y consulta sus citas médicas")

Person(medico, "Médico / Terapista", "Gestiona agenda y registra historia clínica")

Person(agendador, "Agendador de Citas", "Funcionario que agenda manualmente")

Person(admin, "Administrador", "Gestiona usuarios, médicos y reportes")

System(sistema, "Sistema de Agendamiento de Citas Médicas (Aplicación de Escritorio)", "Aplicación monolítica en capas (MVC) para gestión de citas e historias clínicas")

Rel(paciente, sistema, "Agenda y consulta citas")
Rel(medico, sistema, "Gestiona agenda y registra historia clínica")
Rel(agendador, sistema, "Registra y reprograma citas")
Rel(admin, sistema, "Administra el sistema")

@enduml

## Container Diagram

@startuml Containers
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

title Sistema de Agendamiento - Nivel 2 (Monolito MVC Desktop)

Person(paciente, "Paciente")
Person(medico, "Médico")
Person(agendador, "Agendador")
Person(admin, "Administrador")

System_Boundary(s1, "Sistema de Agendamiento") {

  Container(app, "Aplicación Desktop", "Java Desktop Swing", "Aplicación monolítica en capas (MVC) que gestiona citas, historias clínicas y usuarios")

  ContainerDb(db, "Base de Datos", "SQLite", "Almacena citas, usuarios, historias clínicas y auditoría")

}


Rel(paciente, app, "Usa")
Rel(medico, app, "Usa")
Rel(agendador, app, "Usa")
Rel(admin, app, "Usa")

Rel(app, db, "Lee/Escribe", "JDBC")


@enduml

## Component Diagram

@startuml Components
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml

title Sistema de Agendamiento - Nivel 3 (Componentes Monolito MVC)

Container(app, "Aplicación Desktop", "Java Desktop Swing") {

  Component(ui, "Capa de Presentación", "Swing (Views + Controllers)", "Interfaz gráfica y controladores que manejan eventos del usuario")

  Component(appService, "Capa de Aplicación", "Servicios / Casos de Uso", "Orquesta la lógica del negocio y coordina operaciones")

  Component(domain, "Capa de Dominio", "Entidades y Reglas de Negocio", "Contiene la lógica central del negocio (Cita, Usuario, HistoriaClinica)")

  Component(repository, "Capa de Infraestructura", "DAO / Repositories (JDBC)", "Implementa persistencia y acceso a datos")

}

ContainerDb(db, "Base de Datos", "SQLite")

Rel(ui, appService, "Invoca casos de uso")
Rel(appService, domain, "Aplica reglas de negocio")
Rel(appService, repository, "Solicita persistencia")
Rel(repository, db, "Lee/Escribe", "JDBC")

@enduml