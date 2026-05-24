# Proyecto Gestión de Eventos

## Integrantes
- Juan David Cardona Petrel
- Santiago Barrero López

# Descripción del Proyecto

Aplicación desarrollada en JavaFX para la gestión de eventos, compras y administración de recintos.  
El sistema permite:

- Gestión de usuarios y autenticación.
- Exploración y compra de eventos.
- Administración de recintos, zonas y asientos.
- Gestión de compras y pagos.
- Generación de reportes.
- Panel administrativo con métricas.

# Tecnologías Utilizadas

- Java 17
- JavaFX
- Maven
- Scene Builder

# Patrones de Diseño Implementados
## Creacionales: Singleton, Factory y Builder

Propósito Singleton: garantizar una única instancia compartida.
Uso: manejo centralizado de datos y servicios.

Propósito Factory: creación de distintos tipos de usuarios. Clase: UsuarioFactory.

Builder
Propósito Builder: construcción flexible de compras complejas. Clase: CompraBuilder.

## Estructurales: Decorator, Adapter y Facade

Propósito Decorator: agregar funcionalidades adicionales a una compra.
Clases: VIPDecorator, SeguroDecorator, MerchandisingDecorator.

Propósito Adapter: integrar diferentes métodos de pago.
Clases: PayPalAdapter, StripeAdapter.

Propósito: simplificar el proceso de compra.
Clase: CompraFacade.

## Comportamiento: Strategy, State y Observer
Propósito Strategy: permitir diferentes estrategias de pago.
Interfaces y clases: IPago, PagoPaypal, PagoStripe.

Propósito State: manejar estados de compra.
Clases: EstadoCreada, EstadoPagada, EstadoCancelada.

Propósito Observer: notificar cambios y eventos del sistema.
Interfaces: IObservador, ISujeto.

# Principios SOLID Aplicados
## SRP (Single Responsibility Principle)
Separación entre modelos, servicios y controladores.
## OCP (Open/Closed Principle)
Uso de Decorator y Strategy para extender funcionalidades sin modificar clases existentes.
## DIP (Dependency Inversion Principle)
Uso de interfaces como:

- IEventoService
- ICompraService
- ISP (Interface Segregation Principle)

Interfaces específicas para cada servicio del sistema.

# Funcionalidades Principales
- Registro e inicio de sesión.
- Gestión de eventos.
- Gestión de compras.
- Gestión de recintos, zonas y asientos.
- Gestión de incidencias.
- Reportes operativos.
- Panel administrativo.

# Datos Iniciales

El proyecto incluye datos precargados para:

- Usuarios
- Eventos
- Recintos
- Zonas
- Compras
- Pagos
